package cn.ssh.bos.action.bc;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.bc.Standard;
import cn.ssh.bos.domain.user.User;
import cn.ssh.bos.service.facade.FacadeService;

@Controller("standardAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/bc")
public class StandardAction extends BaseAction<Standard> {

	@Autowired
	private FacadeService facadeService;
	//staffAction_save
	@Action(value="standardAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/base/standard.jsp")})
	public String save(){
		try {
			User exitUser = (User) getSessionAttribute("loginUser");
			Standard standard = getModel();
			standard.setOperator(exitUser.getEmail());
			
			standard.setOperationtime(new Date(System.currentTimeMillis()));
			standard.setOperatorcompany(exitUser.getStation());
			facadeService.getStandardService().save(standard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}
	//分页查询
	@Action(value="standardAction_pageDate")
	public String pageDate(){
		
		Page<Standard> pagedate=facadeService.getStandardService().findPageDate(getPageable());
			setPagedate(pagedate);
		return "pageDate";
	}
	
	//批量作废收货标准
	@Action(value="standardAction_deltag" ,results={@Result(name="deltag",type="json")})
	public String deltag(){
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] sid = ids.split(",");
				for (String id : sid) {
					facadeService.getStandardService().deltag(Integer.parseInt(id));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "deltag";
	}
	//批量还原收货标准
	@Action(value="standardAction_backtag" ,results={@Result(name="backtag",type="json")})
	public String backtag(){
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] sid = ids.split(",");
				for (String id : sid) {
					facadeService.getStandardService().backtag(Integer.parseInt(id));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "backtag";
	}
	//查询所有收货标准
	@Action(value="standardAction_findAllStandard" ,results={@Result(name="findAllStandard",type="json")})
	public String findAllStandard(){
		try {
			List<Standard> list=facadeService.getStandardService().findAll();
			push(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
}
