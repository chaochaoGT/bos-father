package cn.ssh.bos.action.auth;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.auth.Function;
import cn.ssh.bos.domain.auth.Menu;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller("menuAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/menu")
public class MenuAction extends BaseAction<Menu> {

	@Autowired
	private FacadeService facadeService;
	//functionAction_save添加权限功能
	@Action(value="menuAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/admin/menu.jsp")})
	public String save(){
		try {
			if (getModel().getMenu() == null|| StringUtils.isBlank(getModel().getMenu().getId()) ){
				getModel().setMenu(null);
			}
			facadeService.getMenuService().save(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}

	//条件和进行重载分页查询functionAction_pageQuery
	@Action(value="menuAction_pageQuery")
	public String pageQuery(){
		setPage(Integer.parseInt(getParameter("page")));

		Page<Menu> pagedate=facadeService.getMenuService().findPageDate(getPageable());
			setPagedate(pagedate);
		return "pageDate";
	}

	//查询所有菜单
		@Action(value="menuAction_ajaxListHasSonMenus" ,results={@Result(name="ajaxListHasSonMenus",
				type ="fastJson", params = { "includeProperties", "id,name" })})
		public String ajaxListHasSonMenus(){
			try {
				List<Menu> list=facadeService.getMenuService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ajaxListHasSonMenus";
		}//查询所有菜单
		@Action(value="menuAction_ajaxList" ,results={@Result(name="ajaxList",
				type ="fastJson", params = { "includeProperties", "id,pId,name,page" })})
		public String ajaxList(){
			try {
				List<Menu> list=facadeService.getMenuService().ajaxList();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ajaxList";
		}
		//查询所有收货标准
		@Action(value="menuAction_findMenuByUid" ,results={@Result(name="findMenuByUid",
				type ="fastJson", params = { "includeProperties", "id,pId,name,page" })})
		public String findMenuByUid(){
			try {
				Subject sub = SecurityUtils.getSubject();
				User user = (User) sub.getPrincipal();

				List<Menu> list=facadeService.getMenuService().findMenuByUid(user.getId());
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "findMenuByUid";
		}
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
//
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
