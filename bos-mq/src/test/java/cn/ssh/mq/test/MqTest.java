package cn.ssh.mq.test;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:applicationContest-mq.xml")
public class MqTest {

//	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	//引入p2p和topic生产者
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate queueSender;


	@Test
	public void testQueue1(){
//	String telephone = ((MapMessage)message).getString("telephone");
//	String staffname = ((MapMessage)message).getString("staffname");
//	String username = ((MapMessage)message).getString("username");
//	String stel = ((MapMessage)message).getString("stel");

		queueSender.send("bos_customer",  new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", "17612184868");
				mapMessage.setString("staffname", "staffname");
				mapMessage.setString("username", "username");
				mapMessage.setString("stel", "15850002095");
				return  mapMessage;
			}
		});
	}@Test
	public void testQueue(){

//		String telephone = ((MapMessage)message).getString("telephone");
//		String staffname = ((MapMessage)message).getString("staffname");
//		String username = ((MapMessage)message).getString("username");
//		String utel = ((MapMessage)message).getString("utel");
//		String addr = ((MapMessage)message).getString("addr");
		queueSender.send("bos_staff",  new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", "17612184868");
				mapMessage.setString("staffname", "staffname");
				mapMessage.setString("username", "username");
				mapMessage.setString("utel", "15850002095");
				mapMessage.setString("addr", "我不知道地址");
				return  mapMessage;
			}
		});
	}
	
}
