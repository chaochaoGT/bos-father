package cn.ssh.bos.action.bc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.bc.Standard;
import cn.ssh.bos.service.facade.FacadeService;

@Controller("staffAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/staff")
public class StaffAction extends BaseAction<Staff> {

	@Autowired
	private FacadeService facadeService;
	//staffAction_save增加收货员
	@Action(value="staffAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/base/staff.jsp")})
	public String save(){
		try {
			facadeService.getStaffService().save(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}
	
	//查询所有收货标准
		@Action(value="staffAction_findAllStaffName" ,results={@Result(name="findAllStaffName",
				type ="fastJson", params = { "includeProperties", "name" })})
		public String findAllStaffName(){
			try {
				List<Standard> list=facadeService.getStandardService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "findAllStaffName";
		}
		//查询所有收货员
		@Action(value="staffAction_findStaffName" ,results={@Result(name="findStaffName",
				type ="fastJson", params = { "includeProperties", "id,name" })})
		public String findStaffName(){
			try {
				List<Staff> list=facadeService.getStaffService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "findStaffName";
		}
		//查询所有收货员
		@Action(value="staffAction_ajaxStaffList" ,results={@Result(name="ajaxStaffList",
				type ="fastJson", params = { "includeProperties", "id,station" })})
		public String ajaxStaffList(){
			try {
				List<Staff> list=facadeService.getStaffService().findAll();
				push(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ajaxStaffList";
		}
	//添加验证手机号是否唯一staffAction_ajaxTelephone
	@Action(value="staffAction_ajaxTelephone" ,results={@Result(name="ajaxTelephone",type="json")})
	public String ajaxTelephone(){
		Staff exitStaff=facadeService.getStaffService().findBytelephone(getModel().getTelephone());
		if (exitStaff==null) {
			push(true);
		}else{
			push(false);
		}
		return "ajaxTelephone";
	}
	
	//条件和进行重载分页查询staffAction_pageDate
	@Action(value="staffAction_pageDate")
	public String pageDate(){
		Specification<Staff> spec=new Specification<Staff>() {
			public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//两种查询方式,query(HQL)和(QBC)	此处使用QBC查询
				List<Predicate> predicates=new ArrayList<>();
				if (StringUtils.isNotBlank(getModel().getName())) {
					predicates.add(cb.like(root.get("name").as(String.class), "%"+getModel().getName()+"%"));
				}
				if (StringUtils.isNotBlank(getModel().getTelephone())) {
					predicates.add(cb.equal(root.get("telephone").as(String.class), getModel().getTelephone()));
				}
				if (StringUtils.isNotBlank(getModel().getStation())) {
					predicates.add(cb.equal(root.get("station").as(String.class),"%" +getModel().getStation()+"%"));
				}
				if (StringUtils.isNotBlank(getModel().getStandard())) {
					predicates.add(cb.equal(root.get("standard").as(String.class), getModel().getStandard()));
				}
				Predicate[] ps=new Predicate[predicates.size()];
				return cb.and(predicates.toArray(ps));
			}
		};
		Page<Staff> pagedate=facadeService.getStaffService().findPageDate(spec,getPageable());
			setPagedate(pagedate);
		return "pageDate";
	}
	
	//staffAction_deltag作废
	//批量作废收货标准
		@Action(value="staffAction_deltag" ,results={@Result(name="deltag",type="json")})
		public String deltag(){
			try {
				String ids = getParameter("ids");
				if (StringUtils.isNotBlank(ids)) {
					String[] sid = ids.split(",");
					for (String id : sid) {
						facadeService.getStaffService().deltag(id);
					}
				}
				push(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				push(false);
			}
			
			return "deltag";
		}
		
		//批量还原staffAction_backtag
		@Action(value="staffAction_backtag" ,results={@Result(name="backtag",type="json")})
		public String backtag(){
			try {
				String ids = getParameter("ids");
				if (StringUtils.isNotBlank(ids)) {
					String[] sid = ids.split(",");
					for (String id : sid) {
						facadeService.getStaffService().backtag(id);
					}
				}
				push(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				push(false);
			}
			return "backtag";
		}
		
}
