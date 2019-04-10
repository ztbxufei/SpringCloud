package com.zhatianbang.directexchange;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 消息接收者
 * @author Administrator
 * @RabbitListener bindings:绑定队列
 * @QueueBinding  value:绑定队列的名称
 *                exchange:配置交换器
 *                key:队列路由
 * 
 * @Queue value:配置队列名称
 *        autoDelete:是否是一个可删除的临时队列
 * 
 * @Exchange value:为交换器起个名称
 *           type:指定具体的交换器类型
 */
@Component
@RabbitListener(
			bindings=@QueueBinding(
					value=@Queue(value="${mq.config.direct.queue.info}",autoDelete="true"),
					exchange=@Exchange(value="${mq.config.direct.exchange}",type= ExchangeTypes.DIRECT),
					key="${mq.config.direct.queue.info.routing.key}"
			)
		)
public class InfoReceiver {

	/**
	 * 接收消息的方法。采用消息队列监听机制
	 * @param msg
	 */
	@RabbitHandler
	public void process(String msg){
		System.out.println("Info........receiver: "+msg);
	}
}
