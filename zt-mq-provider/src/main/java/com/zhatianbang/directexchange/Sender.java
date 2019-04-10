package com.zhatianbang.directexchange;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 * @author zm
 *
 */
@Component
public class Sender {

	@Autowired
	private AmqpTemplate rabbitAmqpTemplate;
	
	//exchange 交换器名称
	@Value("${mq.config.direct.exchange}")
	private String exchange;
	
	//error队列 routingkey 路由键
	@Value("${mq.config.direct.queue.error.routing.key}")
	private String routingkey;

	//error队列 routingkey 路由键
	@Value("${mq.config.direct.queue.info.routing.key}")
	private String info_routingkey;

	/*
	 * 发送error消息的方法
	 */
	public void sendError(String msg){
		//向消息队列发送消息
		//参数一：交换器名称。
		//参数二：路由键
		//参数三：消息
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, this.routingkey, msg);
	}

	/*
	 * 发送info消息的方法
	 */
	public void sendInfo(String msg){
		//向消息队列发送消息
		//参数一：交换器名称。
		//参数二：路由键
		//参数三：消息
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, this.info_routingkey, msg);
	}
}
