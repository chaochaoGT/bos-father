package cn.ssh.bos.action.inteceptor.user;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import cn.ssh.bos.domain.user.User;

@Component("loginInteceptor")
public class LoginInteceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		User exitUser= (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
		if (exitUser==null) {
			return "no_login";
		}
		return invocation.invoke();
	}

}
