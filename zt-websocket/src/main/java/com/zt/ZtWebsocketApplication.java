package com.zt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public  class ZtWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtWebsocketApplication.class, args);
	}

}
