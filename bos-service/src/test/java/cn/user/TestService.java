package cn.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:applicationContext-domain.xml",
		"classpath:applicationContext-dao.xml",
		"classpath:applicationContext-service.xml"})
public class TestService {
	
	@Autowired
//	private IUserService userService;
	private FacadeService facadeService; 
	
	@Test
	public void test1(){
		User u = new User();
		u.setEmail("123@123.com");
		u.setTelephone("sd212");
		u.setPassword("111");
		facadeService.getUserService().save(u);
//		userService.save(u);
	}
	@Test
	public void test2(){
		User u = new User();
		u.setEmail("123@123.com");
		u.setPassword("111");
		System.out.println(facadeService.getUserService().login(u.getEmail(), u.getPassword()));
//		userService.save(u);
	}

}
