package com.vrv.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
 
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {
 
	@Autowired
	private CallBackSender sender;
 
	@Test
	public void topic() throws Exception {
		sender.send("topic.baqgl.admin.1", "测试消息");
	}
}
