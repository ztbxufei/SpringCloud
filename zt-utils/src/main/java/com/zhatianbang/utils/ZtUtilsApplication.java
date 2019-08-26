package com.zhatianbang.utils;

import com.zhatianbang.utils.socket.nio.NIOServerSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ZtUtilsApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ZtUtilsApplication.class, args);
        applicationContext.getBean(NIOServerSocket.class).start();
    }

}
