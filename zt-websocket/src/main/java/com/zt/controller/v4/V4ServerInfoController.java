package com.zt.controller.v4;

import com.zt.service.impl.SockJSWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


/**
 * 
 * 功能描述： 实时推送服务器的JVM负载，已用内存等消息
 *
 */
@Controller
public class V4ServerInfoController {

	@Autowired
	SockJSWebSocketService sockJSWebSocketService;
	
	
	@MessageMapping("/v4/schedule/push")
	@Scheduled(fixedRate = 3000)  //方法不能加参数
	public void sendServerInfo(){
		sockJSWebSocketService.sendServerInfo();
	}
	
	
}
