package com.zhatianbang.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ZtItemServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZtItemServiceApplication.class, args);
	}

}
