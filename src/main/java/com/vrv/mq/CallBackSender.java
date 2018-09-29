package com.vrv.mq;


import java.nio.charset.Charset;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CallBackSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send( String routingKey, String message) {
		rabbitTemplate.setConfirmCallback(this);
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

		System.out.println("send 消息id:" + correlationData.getId());
		// 用RabbitMQ发送MQTT需将exchange配置为amq.topic
//		this.rabbitTemplate.convertAndSend("amq.topic", topic, message, correlationData);
		
		
		// 传过来的参数topic为topic.baqgl.admin.1
		// final static String ROUTING_KEY = "topic.baqgl.#";
		this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, routingKey, message, correlationData);
	}

	/**
     * 确认Broker接收消息的状态
     * 本次测试是，只要到了Exchange就算是发送成功，更是否到达Queue没有关系，(把queue绑定的方法去掉，public Binding binding() 就可以测试), 消息有打印到控制台
     * basic.ack
     */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		System.out.println("confirm 消息id:" + correlationData.getId() );
		if (ack) {
			System.out.println("消息发送确认成功, success");
		} else {
			System.out.println("消息发送确认失败， failed:" + cause);
		}
	}

	 /**
     * 在Mandatory下，当exchange存在但无法路由至queue的情况下记录入库
     * <p>
     * basic.return（basic.return将会发生在basic.ack之前）
     */
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		final String failedMessage = new String(message.getBody(), Charset.forName("UTF-8"));
		final String text = failedMessage.toString();
		// 因为在basic.return之后会调用basic.ack，鄙人认为NO_ROUTE的状态有可能被错误地转换成为NOT_FOUND，所以不需要考虑竞争情况
		System.out.println("returned message>>>>>>>>:" + text);
	}
}
