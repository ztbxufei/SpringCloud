package com.zhatian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author: xf
 * @Date: 2019/03/03 15:25
 * @Description: 注册中心
 */
@SpringBootApplication
@EnableEurekaServer
public class ZtRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtRegistryApplication.class, args);
	}

}
