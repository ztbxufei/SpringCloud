package com.zhatianbang.utils.socket.nio;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * socket 通过nio 实现tcp/ip 通讯的客户端，该客户端仅作为服务端测试客户端
 */
public class NIOClient {

    /**消息格式如下**/
    /**
     * 协议格式：消息包头+XML消息包(消息包头一定要带有数据长度)
     *
     * 消息头格式：消息包起始符（四个字符串’KDLA’）（消息起始符自行约定） +XML 包长度数据（将数字转换为八位 十进制字符串）；例如：长度为 260 的包数据为’KDLA00000260’；
     * 注：
     * ① 双方所有发送数据都必须遵循协议格式
     * ② 长度不包含消息包头长度
     *
     * XML 格式如下：（消息只要求是xml格式即可）
     * <?xml version="1.0" encoding="gb2312">
     * <MESSAGE NAME="MSGNAME" TYPE="REQ/RSP" SEQNO="SEQNO">
     * <NODENAME>NODEVALUE</NODENAME>
     * </MESSAGE>
     */

    //约定消息数据字符集
    private final String charsetName_read = "gb2312";

    //服务端ip
    private String serverIp = "127.0.0.1";

    //服务端端口
    private int serverProt = 9844;

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            //连接服务端socket
            SocketAddress socketAddress = new InetSocketAddress(serverIp, serverProt);
            socketChannel.connect(socketAddress);
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

            /**以下是通过dom4j进行元素添加  再转成xml**/
            Document document = DocumentHelper.createDocument();
            document.setXMLEncoding("gb2312");
            Element element = document.addElement("MESSAGE");
            element.addAttribute("xxxx","xxxxx");
            Element message = document.getRootElement();
            message.addElement("xxx").setText("xxx");
            String xmlStr = document.asXML();
            /**以上是通过dom4j进行元素添加  再转成xml**/

            String msg = "KDLA" + String.format("%08d",xmlStr.getBytes(charsetName_read).length) + xmlStr.trim();

            buffer.clear();
            buffer.put(msg.getBytes(charsetName_read));int count = 1;
            //读取模式
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();

            //从服务端读取消息
            int readLenth = socketChannel.read(buffer);
            //读取模式
            buffer.flip();
            byte[] bytes = new byte[readLenth];
            buffer.get(bytes);
            System.out.println(new String(bytes, charsetName_read));
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args){
        NIOClient client = new NIOClient();
        client.start();
    }
}
