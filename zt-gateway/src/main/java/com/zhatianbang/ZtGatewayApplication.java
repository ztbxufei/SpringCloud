package com.zhatianbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
//启动类添加zuul启动类注解，提供zuul支持
@EnableZuulProxy
@EnableDiscoveryClient
public class ZtGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtGatewayApplication.class, args);
	}

}
