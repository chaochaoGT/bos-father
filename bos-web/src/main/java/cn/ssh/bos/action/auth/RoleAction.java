package cn.ssh.bos.action.auth;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.auth.Function;
import cn.ssh.bos.domain.auth.Role;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.service.facade.FacadeService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller("roleAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/role")
public class RoleAction extends BaseAction<Role> {

	@Autowired
	private FacadeService facadeService;
	//functionAction_save添加权限功能
	@Action(value="roleAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/admin/role.jsp")})
	public String save(){
		try {
            String menuIds = getParameter("menuIds");
            String[] fids = getRequset().getParameterValues("functionIds");
            facadeService.getRoleService().save(getModel(),menuIds,fids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}

	//条件和进行重载分页查询functionAction_pageQuery
	@Action(value="roleAction_pageQuery")
	public String pageQuery(){

		Page<Role> pagedate=facadeService.getRoleService().findPageDate(getPageable());
			setPagedate(pagedate);
		return "pageDate";
	}

    		//查询所有收货员
		@Action(value="roleAction_ajaxList" ,results={@Result(name="ajaxList",
				type ="fastJson", params = { "includeProperties", "id,name" })})
		public String ajaxList(){
			try {
				List<Role> list=facadeService.getRoleService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ajaxList";
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
//		@Action(value="staffAction_ajaxStaffList" ,results={@Result(name="ajaxStaffList",
//				type ="fastJson", params = { "includeProperties", "id,station" })})
//		public String ajaxStaffList(){
//			try {
//				List<Staff> list=facadeService.getStaffService().findAll();
//				push(list);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return "ajaxStaffList";
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
