package com.zhatianbang.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ZtSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtSearchApplication.class, args);
	}

}
