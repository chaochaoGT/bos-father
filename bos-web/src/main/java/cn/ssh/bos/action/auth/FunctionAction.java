package cn.ssh.bos.action.auth;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.auth.Function;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.bc.Standard;
import cn.ssh.bos.service.facade.FacadeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Controller("functionAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/function")
public class FunctionAction extends BaseAction<Function> {

	@Autowired
	private FacadeService facadeService;
	//functionAction_save添加权限功能
	@Action(value="functionAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/admin/function.jsp")})
	public String save(){
		try {
			facadeService.getFunctionService().save(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}

	//条件和进行重载分页查询functionAction_pageQuery
	@Action(value="functionAction_pageQuery")
	public String pageQuery(){

		Page<Function> pagedate=facadeService.getFunctionService().findPageDate(getPageable());
			setPagedate(pagedate);
		return "pageDate";
	}

			//查询所有收货员
		@Action(value="functionAction_ajaxList" ,results={@Result(name="ajaxStaffList",
				type ="fastJson", params = { "includeProperties", "id,name" })})
		public String ajaxStaffList(){
			try {
				List<Function> list=facadeService.getFunctionService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ajaxStaffList";
		}
//	//查询所有收货标准
//		@Action(value="staffAction_findAllStaffName" ,results={@Result(name="findAllStaffName",
//				type ="fastJson", params = { "includeProperties", "name" })})
//		public String findAllStaffName(){
//			try {
//				List<Standard> list=facadeService.getStandardService().findAll();
//				push(list);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return "findAllStaffName";
//		}
//		//查询所有收货员
//		@Action(value="staffAction_findStaffName" ,results={@Result(name="findStaffName",
//				type ="fastJson", params = { "includeProperties", "id,name" })})
//		public String findStaffName(){
//			try {
//				List<Staff> list=facadeService.getStaffService().findAll();
//				push(list);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return "findStaffName";
//		}

//	//添加验证手机号是否唯一staffAction_ajaxTelephone
//	@Action(value="staffAction_ajaxTelephone" ,results={@Result(name="ajaxTelephone",type="json")})
//	public String ajaxTelephone(){
//		Staff exitStaff=facadeService.getStaffService().findBytelephone(getModel().getTelephone());
//		if (exitStaff==null) {
//			push(true);
//		}else{
//			push(false);
//		}
//		return "ajaxTelephone";
//	}
	

	
	//staffAction_deltag作废
	//批量作废收货标准
//		@Action(value="staffAction_deltag" ,results={@Result(name="deltag",type="json")})
//		public String deltag(){
//			try {
//				String ids = getParameter("ids");
//				if (StringUtils.isNotBlank(ids)) {
//					String[] sid = ids.split(",");
//					for (String id : sid) {
//						facadeService.getStaffService().deltag(id);
//					}
//				}
//				push(true);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				push(false);
//			}
//
//			return "deltag";
//		}
//
//		//批量还原staffAction_backtag
//		@Action(value="staffAction_backtag" ,results={@Result(name="backtag",type="json")})
//		public String backtag(){
//			try {
//				String ids = getParameter("ids");
//				if (StringUtils.isNotBlank(ids)) {
//					String[] sid = ids.split(",");
//					for (String id : sid) {
//						facadeService.getStaffService().backtag(id);
//					}
//				}
//				push(true);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				push(false);
//			}
//			return "backtag";
//		}
		
}
