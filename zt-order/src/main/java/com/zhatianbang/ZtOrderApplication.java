package com.zhatianbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// 使用Feign启动类添加注解
@EnableFeignClients
//使用HyStrix做熔断降级，添加@EnableCircuitBreaker注解
@EnableCircuitBreaker
public class ZtOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtOrderApplication.class, args);
	}
	/**
	 * 使用ribbon
	 * 启动类增加注解
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
