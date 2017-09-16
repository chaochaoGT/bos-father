package cn.ssh.bos.action.qp;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.crm.Customers;
import cn.ssh.bos.domain.qp.City;
import cn.ssh.bos.domain.qp.NoticeBill;
import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;

@SuppressWarnings("all")
@Controller("noticeBillAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/noticeBill")
public class NoticeBillAction extends BaseAction<NoticeBill> {
	
	@Autowired
	private FacadeService facadeService;
	
	 //staffAction_save增加分区信息
		@Action(value = "noticeBillAction_save", results = {
				@Result(name = "save", location = "/WEB-INF/pages/qupai/noticebill_add.jsp") })
		public String save() {
			//获取分区id
			User exituser = (User) getSessionAttribute("loginUser");
			
			String province=getRequset().getParameter("nprovince");
			String city=getRequset().getParameter("ncity");
			String district=getRequset().getParameter("ndistrict");
			getModel().setUser(exituser);
			getModel().setArrivecity(province+city+district);
			facadeService.getNoticeBillService().save(getModel(),province,city,district);
			return "save";
		}
	//	redis查询城市信息
	@Action(value="noticeBillAction_ajaxCustomer",results = { @Result(name = "ajaxCustomer", 
			type = "fastJson") })
	public String ajaxCustomer(){
		try {
			Customers customers=facadeService.getNoticeBillService().getCustomer(getModel().getTelephone());
				push(customers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ajaxCustomer";
	}

		
}
