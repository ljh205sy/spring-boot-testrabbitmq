package com.vrv.mq;

/**
 * Hello world!
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
@Configuration
@RestController  
@EnableAutoConfiguration  
@ComponentScan
@SpringBootApplication
public class Application {
 
	@Autowired
	private CallBackSender sender;
 
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
 
	/**
	 * 2018年9月29日 22:59:34  验证通过的mq测试
	 */
	 @RequestMapping("/callback")
	public void callbak() {
		sender.send("topic.baqgl.admin.11111", "测试消息1");// 进入队列的
		sender.send("topic.baqgl.11111.admin", "测试消息2");// 进入队列的，是因为routingKey为topic.baqgl.11111.admin，  bindingkey为topic.baqgl.#
		sender.send("11111.admin", "测试消息3"); // 丢弃此消息， 此消息是没有进入队列的，是因为routingKey为11111.admin，  bindingkey规则必须为（topic.baqgl.#）类型的
		sender.send("2222.admin", "测试消息4");  // 进入队列的
		sender.send("2222.admin.111test", "测试消息5");  // 不会进入队列，因为只匹配"2222.admin"，如果是"2222.admin.#"则会进入队列中
	}
}