package cn.ssh.bos.action.user;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;
import cn.ssh.bos.uitl.alidayu.RandCodeUtils;
@Controller
@Scope("prototype")
@Namespace("/user")
@ParentPackage("bos")
public class UserAction extends BaseAction<User> {
	
	@Autowired
	private FacadeService facadeService;
	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	//	userAction_checkcode验证码
	@Action(value="userAction_checkcode",results={@Result(name="code",type="fastJson")})
	public String checkcode(){
		String  scode = (String) getSessionAttribute("key");
		String rcode = getParameter("checkcode");
		//一次型验证码
		System.out.println("执行验证码");
		if (scode.equalsIgnoreCase(rcode)) {
			push(true);
		}else{
			push(false);
		}
		return "code";
	}
	
	
	//userAction_login 采用shiro
	@Action(value="userAction_login",results={@Result(name="login_ok",type="redirect",location="/index.jsp"),
			@Result(name="login_fail",location="/login.jsp"),
			@Result(name="login_param_error",location="/login.jsp")
			})
	@InputConfig(resultName="login_param_error")
	public String login(){

        try {
            Subject subject= SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(getModel().getEmail(),getModel().getPassword());
            subject.login(token);
            return "login_ok";
        }catch (UnknownAccountException e){
            e.printStackTrace();
            addActionError(getText("login_username_error"));
            return "login_fail";
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            addActionError(getText("login_password_error"));
            return "login_fail";
        }
	}
	@Action(value="userAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/admin/userlist.jsp")})
	public String save(){
		try {
			String[] rids = getRequset().getParameterValues("roleIds");
			facadeService.getUserService().save(getModel(),rids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}

	//条件和进行重载分页查询functionAction_pageQuery
	@Action(value="userAction_pageQuery")
	public String pageQuery(){

		Page<User> pagedate=facadeService.getUserService().findPageDate(getPageable());
		setPagedate(pagedate);
		return "pageDate";
	}

	//删除用户
	@Action(value="userAction_deleteUser" ,results={@Result(name="deleteUser",type="json")})
	public String deleteUser(){
		try {
			String ids = getParameter("ids");
			facadeService.getUserService().deleteUser(ids);
			push(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}
		return "deleteUser";
	}
	//用户邮箱唯一性校验
	@Action(value="userAction_ajaxUserEmail" ,results={@Result(name="ajaxUserEmail",type="json")})
	public String ajaxUserEmail(){
		try {
			User user=facadeService.getUserService().ajaxUserEmail(getModel());
			if (user==null){
				push(true);
			}else{
				push(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "ajaxUserEmail";
	}//用户邮箱唯一性校验
	@Action(value="userAction_ajaxTelephone" ,results={@Result(name="ajaxTelephone",type="json")})
	public String ajaxTelephone(){
		try {
			User user=facadeService.getUserService().ajaxTelephone(getModel());
			if (user==null){
				push(true);
			}else{
				push(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}
		return "ajaxTelephone";
	}
// userAction_login
//	@Action(value="userAction_login",results={@Result(name="login_ok",type="redirect",location="/index.jsp"),
//			@Result(name="login_fail",location="/login.jsp"),
//			@Result(name="login_param_error",location="/login.jsp")
//			})
//	@InputConfig(resultName="login_param_error")
//	public String login(){
//		User user = getModel();
//		User exitUser=facadeService.getUserService().login(user.getEmail(), user.getPassword());
//		//一次性验证码
//		removeSessionAttribute("key");
//		if (exitUser==null) {
//			addActionError(this.getText("login_error_message"));
//			return "login_fail";
//		}else{
//			setSessionAttribute("loginUser", exitUser);
//
//			return "login_ok";
//		}
//	}
	
	//退出 userAction_outLogin
	@Action(value="userAction_outLogin",results={@Result(name="out_login",type="redirect",location="/login.jsp")})
	public String outLogin(){
		//removeSessionAttribute("loginUser");
        Subject subject=SecurityUtils.getSubject();
		System.out.println("logout="+subject);
		subject.logout();
        return "out_login";
	}
	
	//生成验证码userAction_sendSms
	@Action(value="userAction_sendSm",results={@Result(name="sendSms_ok",type="fastJson")})
	public String sendSms(){
		System.out.println("开始执行了-->sendSms");
		try {
		//获取电话号码和生成随机验证码
		final String tel=getModel().getTelephone();
		final String code=RandCodeUtils.getCode();
		//存入到Redis
		RedisTemplate<Serializable, Serializable> redisTemplate = facadeService.getRedisTemplate();
		redisTemplate.opsForValue().set(tel, code, 120, TimeUnit.SECONDS);
		//发送消息对列到mq,发短信
		JmsTemplate jmsTemplate = facadeService.getJmsTemplate();
		jmsTemplate.send("bos_smc",  new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telphone", tel);
					mapMessage.setString("code", code);
					return  mapMessage;
				}
			});
		System.out.println("结束执行了-->sendSms//"+tel+":"+code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "sendSms_ok";
	}
	
	//短信,验证码检验userAction_smsPassword
	// return 返回值设定 0,验证码错误, 1短信验证码校验正确 ,2 验证码超时 ,3用户不存在 
	@Action(value="userAction_smsPassword",results={@Result(name="smsPassword",type="fastJson")})
	public String smsPassword(){
		try {
			User exitUser = facadeService.getUserService().findByTelephone(getModel().getTelephone());
			if (exitUser==null) {
				push("3");
			}else{
				RedisTemplate<Serializable, Serializable> redisTemplate = facadeService.getRedisTemplate();
				String code = getRequset().getParameter("checkcode");
				String  redisCode = (String) redisTemplate.opsForValue().get(getModel().getTelephone());
				if (StringUtils.isNotBlank(redisCode)) {
					if (redisCode.equals(code)) {
						push("1");
						redisTemplate.delete(getModel().getTelephone());
					}else{
						push("0");
					}
				}else{
					push("2");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "smsPassword";
	}
	
	//短信修改密码userAction_gobackpassword
	@Action(value="userAction_gobackpassword",results={@Result(name="gobackpassword",type="fastJson")})
	public String gobackpassword(){
		User exitUser = facadeService.getUserService().findByTelephone(getModel().getTelephone());
		User exitUser1 = (User) getSessionAttribute("loginUser");
		try {
			if (exitUser!=null) {
				facadeService.getUserService().updateUser(getModel());
				push(true);
			}else if (exitUser1!=null) {
				exitUser1.setPassword(getModel().getPassword());
				facadeService.getUserService().save(exitUser1);
				push(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}

		return "gobackpassword";
	}
	 
	
}
