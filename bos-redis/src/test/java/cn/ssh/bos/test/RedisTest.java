package cn.ssh.bos.test;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-redis.xml")
public class RedisTest {
	
	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	//存数据   测试时解开xml  <context:property-placeholder location="classpath:redis.properties" />
	@Test
	public void test1(){
		redisTemplate.opsForValue().set("18733133356","1111");
	}

	@Test
	public void test2(){
		String name = (String) redisTemplate.opsForValue().get("姓名");
		System.out.println("姓名-->"+name);
	}

	@Test
	public void test(){
		ClassPathXmlApplicationContext c =new ClassPathXmlApplicationContext("applicationContext-redis.xml");
		Object bean = c.getBean("redisTemplate");
		System.out.println(bean);
	}
}
