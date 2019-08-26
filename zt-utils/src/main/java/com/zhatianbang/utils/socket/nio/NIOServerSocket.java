package com.zhatianbang.utils.socket.nio;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * socket 通过nio 实现tcp/ip 通讯的服务端
 *
 */
@Component
public class NIOServerSocket {

    private static Logger logger = LoggerFactory.getLogger(NIOServerSocket.class);

    @Value("${socket.tcp.server.port}")
    private Integer port;

    private volatile boolean started;

    //多路复用器（管理所有的通道）
    private Selector selector;

    //建立读缓冲区，大小自行定义，本例定义了10M内存
    private ByteBuffer readBuf = ByteBuffer.allocate(1024*1024*10);

    //建立写缓冲区，大小自行定义，本例定义了10KB内存
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024*10);

    /*映射客户端发送消息queue  key是客户端ip等唯一信息，value是消息队列*/
    public static Map<String, ConcurrentLinkedQueue> sendQueueMap = new HashMap<>();

    public void start(){
        start(null);
    }

    public void start(Integer port) {
        try {
            //1.开启多路复用器
            this.selector = Selector.open();

            //2.打开服务端通道
            ServerSocketChannel ssc = ServerSocketChannel.open();

            //3.设置服务端通道为非阻塞模式
            ssc.configureBlocking(false);

            //4.绑定地址
            ssc.bind(new InetSocketAddress(port == null ? this.port : port));

            //5.把服务端通道注册到多路复用器上，并且监听阻塞事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("server开启成功");
            started = true;

        } catch (IOException e) {
            logger.info("server开启失败");
        }

        while(started){
            try {
                //1.让多路复用器开始监听
                this.selector.select();

                //2.返回多路复用器已经选择的结果集
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

                //3.进行响应
                while(keys.hasNext()){

                    //4.获取一个选择元素
                    SelectionKey key = keys.next();

                    //5.直接从容器中移除
                    keys.remove();

                    //6.如果key有效
                    if(key.isValid()){

                        //7.如果为阻塞状态
                        if(key.isAcceptable()){
                            this.accept(key);//这里的key就是服务器端的Channel的key
                        }

                        //8.如果为可读状态
                        if(key.isValid() && key.isReadable()){
                            this.read(key);
                        }

                        if(key.isValid() && key.isWritable()){
                            this.write(key);
                        }
                    }
                }
            } catch (IOException e) {
                logger.info("tcp出现异常",e);
            }catch (Exception e) {
                logger.info("出现异常",e);
            }
        }

    }


    /**
     * nio accept方法
     * @param key
     */
    private void accept(SelectionKey key) {
        try {
            //1.获取服务端通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

            //2.执行客户端Channel的阻塞方法
            SocketChannel sc = ssc.accept();

            //3.设置阻塞模式
            sc.configureBlocking(false);

            //4.注册到多路复用器上，并设置读取标识
            sc.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.info("accept出现异常");
        }
    }

    /**
     * nio 从缓冲区读数据的read方法
     * @param key
     * @throws Exception
     */
    private void read(SelectionKey key) throws Exception{
        //获取之前注册的socket通道对象
        SocketChannel sc = (SocketChannel) key.channel();

        this.readBuf.clear();

        //读取数据
        long bytesRead = 0;
        try {
            bytesRead = sc.read(readBuf);
        }catch (Exception e){
            logger.info("从缓冲区读取数据异常",e);
            endBufferStr = "";
            sc.close();
            key.cancel();
            return;
        }

        if(bytesRead > 0){
            //读取之前需要进行复位方法（把position和limit进行复位）
            this.readBuf.flip();

            try {
                handlePacket(sc.socket());
            }catch (Exception e){
                logger.info("handlePacket()处理读取数据异常");
                endBufferStr = "";
            }

            //如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }else{
            sc.close();
            key.cancel();
        }
    }

    /**
     * 将数据写进缓冲区中的write方法  该方法需根据情境重写，本例是从queue中取数据
     * @param key
     * @throws IOException
     */
    private void write(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        String name = sc.socket().getInetAddress().toString().substring(1);
        ConcurrentLinkedQueue nioMessageQueue = sendQueueMap.get(name);
        while(nioMessageQueue != null && nioMessageQueue.peek() != null){
            String message = String.valueOf(nioMessageQueue.poll());
            if(StringUtils.isNotBlank(message)){
                writeBuf.clear();
                writeBuf.put(message.getBytes(charsetName_read));
                //进行复位方法
                writeBuf.flip();
                //将数据写入到信道中
                sc.write(writeBuf);
                logger.info("回复客户端" + name + "的消息：" + message);
            }
        }

        key.interestOps(SelectionKey.OP_READ);
        //为读入更多的数据腾出空间
        writeBuf.compact();
    }

    //断包处理
    private String endBufferStr = "";
    /**数据包头长度 需根据各自的消息头长度赋值**/
    private final int PACKET_HEAD_LENGTH = 12;
    //约定消息数据字符集
    private final String charsetName_read = "gb2312";

    /**
     * 读数据处理方法（断包，粘包，整包的处理方式） 方法中的多行注释部分是消息数据逻辑处理业务代码，应根据各自的情景进行替换注释中的一行代码
     * @param s
     * @throws Exception
     */
    private void handlePacket(Socket s) throws Exception {
        int location = 0;
        int byteBufferSize = 0;
        String message;
        while (readBuf.remaining() >= PACKET_HEAD_LENGTH) {

            if (endBufferStr.length() > 0){
                if(endBufferStr.length() >= PACKET_HEAD_LENGTH){
                    try{
                        byteBufferSize = Integer.parseInt(endBufferStr.substring(4,PACKET_HEAD_LENGTH));
                    }catch (Exception e){
                        logger.info("断包处理中,String转换Integer异常1",e);
                        endBufferStr = "";
                        continue;
                    }
                }else{
                    try{
                        String headerStr = endBufferStr + new String(readBuf.array(), 0, PACKET_HEAD_LENGTH - endBufferStr.length(),charsetName_read);
                        byteBufferSize = Integer.parseInt(headerStr.substring(4,PACKET_HEAD_LENGTH));
                    }catch (Exception e){
                        logger.info("断包处理中,String转换Integer异常2",e);
                        readBuf.position(readBuf.limit());
                        endBufferStr = "";
                        continue;
                    }
                }

                try{
                    location = byteBufferSize + PACKET_HEAD_LENGTH - endBufferStr.getBytes(charsetName_read).length;
                    location = location > readBuf.limit() ? readBuf.limit() : location;
                    endBufferStr = endBufferStr + new String(readBuf.array(), 0, location ,charsetName_read);
                    readBuf.position(location);
                    if (endBufferStr.getBytes(charsetName_read).length >= byteBufferSize+PACKET_HEAD_LENGTH){
                        int outLength = endBufferStr.getBytes(charsetName_read).length - byteBufferSize - PACKET_HEAD_LENGTH;
                        message = endBufferStr.substring(0,endBufferStr.length()-outLength);
                        logger.info("断包处理1message,headMsg="+message.substring(0,12));
                        /**
                         *
                         * 此处代码是一条完整消息数据进行业务逻辑处理
                         * 本例是将消息放入到接收队列中，由相应的事件进行处理，本例如下：
                         * NIOReceiveMessageQueue.getInstance().addMessage(message,s.getInetAddress().toString().substring(1));
                         */

                        endBufferStr = endBufferStr.substring(endBufferStr.length()-outLength);
                    }
                }catch (Exception e){
                    logger.info("断包处理中,readBuf转换String异常",e);
                    readBuf.position(location);
                    endBufferStr = "";
                    continue;
                }

            }else{
                String headerStr = new String(readBuf.array(), location, PACKET_HEAD_LENGTH,charsetName_read);
                try{
                    byteBufferSize = Integer.parseInt(headerStr.substring(4));
                }catch (Exception e){
                    logger.info("String转换Integer异常",e);
                    readBuf.position(readBuf.limit());
                    endBufferStr = "";
                    continue;
                }

                if (readBuf.remaining() >= byteBufferSize+PACKET_HEAD_LENGTH){
                    try{
                        message = new String(readBuf.array(), location, PACKET_HEAD_LENGTH+byteBufferSize,charsetName_read);
                    }catch (Exception e){
                        logger.info("整包或粘包处理中,readBuf转换String异常",e);
                        readBuf.position(location+PACKET_HEAD_LENGTH+byteBufferSize);
                        endBufferStr = "";
                        continue;
                    }
                    logger.info("处理message,headMsg="+message.substring(0,12));
                    /**
                     *
                     * 此处代码是一条完整消息数据进行业务逻辑处理
                     * 本例是将消息放入到接收队列中，由相应的事件进行处理，本例如下：
                     * NIOReceiveMessageQueue.getInstance().addMessage(message,s.getInetAddress().toString().substring(1));
                     */
                    location = location + byteBufferSize + PACKET_HEAD_LENGTH;
                    readBuf.position(location);
                }else{
                    try{
                        endBufferStr = new String(readBuf.array(), location, readBuf.limit() - location,charsetName_read);
                    }catch (Exception e){
                        logger.info("readBuf转换String异常",e);
                        endBufferStr = "";
                    }
                    readBuf.position(readBuf.limit());
                }
            }
        }
        if (readBuf.remaining() > 0){
            endBufferStr = endBufferStr + new String(readBuf.array(), location, readBuf.limit()-location,charsetName_read);
            if (endBufferStr.length() >= PACKET_HEAD_LENGTH){
                try{
                    byteBufferSize = Integer.parseInt(endBufferStr.substring(4,PACKET_HEAD_LENGTH));
                    int outLength = endBufferStr.getBytes(charsetName_read).length - byteBufferSize - PACKET_HEAD_LENGTH;
                    if (outLength >= 0){
                        message = endBufferStr.substring(0,endBufferStr.length()-outLength);
                        logger.info("断包处理2message,headMsg="+message.substring(0,12));
                        /**
                         *
                         * 此处代码是一条完整消息数据进行业务逻辑处理
                         * 本例是将消息放入到接收队列中，由相应的事件进行处理，本例如下：
                         * NIOReceiveMessageQueue.getInstance().addMessage(message,s.getInetAddress().toString().substring(1));
                         */
                        endBufferStr = endBufferStr.substring(endBufferStr.length()-outLength);
                    }
                }catch (Exception e){
                    logger.info("转换异常",e);
                    endBufferStr = "";
                }
            }
            readBuf.position(readBuf.limit());
        }
        readBuf.clear();
    }
}
