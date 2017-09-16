package cn.ssh.bos.action.qp;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.qp.City;
import cn.ssh.bos.service.facade.FacadeService;

@SuppressWarnings("all")
@Controller("loadCityAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/loadCity")
public class LoadCityAction extends BaseAction<City> {
	
	@Autowired
	private FacadeService facadeService;
	
	//loadCityAction_load查询城市信息
//	@Action(value="loadCityAction_load",results={@Result(name="load",
//			type ="fastJson", params = { "includeProperties", "id,name" })})
//	public String load(){
//		try {
//			List<City> list=facadeService.getLoadCityService().load(getModel().getPid());
//			push(list);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "load";
//	}
	//	redis查询城市信息
	@Action(value="loadCityAction_load")
	public String load(){
		try {
			String citys=facadeService.getLoadCityService().loadFromRedis(getModel().getPid());
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			getResponse().getWriter().print(citys);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}

		
}
