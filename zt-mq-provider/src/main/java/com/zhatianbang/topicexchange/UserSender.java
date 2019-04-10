package com.zhatianbang.topicexchange;

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
public class UserSender {

	@Autowired
	private AmqpTemplate rabbitAmqpTemplate;
	
	//exchange 交换器名称
	@Value("${mq.config.topic.exchange}")
	private String exchange;
	

	/*
	 * 发送消息的方法
	 */
	public void UserSend(String msg){
		//向消息队列发送消息
		//参数一：交换器名称。
		//参数二：路由键
		//参数三：消息
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, "user.log.debug", "user.log.debug"+msg);
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, "user.log.info", "user.log.info"+msg);
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, "user.log.error", "user.log.info"+msg);
		this.rabbitAmqpTemplate.convertAndSend(this.exchange, "user.log.warn", "user.log.info"+msg);
	}


}
