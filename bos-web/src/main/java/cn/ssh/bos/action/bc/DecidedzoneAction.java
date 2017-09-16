package cn.ssh.bos.action.bc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.crm.Customers;
import cn.ssh.bos.service.facade.FacadeService;

@Controller("decidedzoneAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/decidedzone")
@SuppressWarnings("all")
public class DecidedzoneAction extends BaseAction<Decidedzone> {

	@Autowired
	private FacadeService facadeService;
	private String[] sid;
	
	public void setSid(String[] sid) {
		this.sid = sid;
	}

	// //staffAction_save增加分区信息
	@Action(value = "decidedzoneAction_save", results = {
			@Result(name = "save", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String save() {
		//获取分区id
		facadeService.getDecidedzoneService().save(sid,getModel());
		return "save";
	}

	// 条件和进行重载分页查询decidedzoneAction_pageDate
	@Action(value = "decidedzoneAction_pageDate")
	public String pageDate() {
		Specification<Decidedzone> spec = getSpecificationSubarea();
		Page<Decidedzone> pagedate = facadeService.getDecidedzoneService().findPageDate(spec,getPageable());
		setPagedate(pagedate);
		return "pageDate";
	}
	//查找未关联客户findNoAssociation
	@Action(value = "decidedzoneAction_findNoAssociation", results = { @Result(name = "findNoAssociation", 
			type = "fastJson",params = { "includeProperties","id,name" }) })
	public String findNoAssociation() {
		
		List<Customers> list=facadeService.getDecidedzoneService().findNoAssociation();
		push(list);
		return "findNoAssociation";
	}
	//查找已关联客户findNoAssociation
	@Action(value = "decidedzoneAction_findUserAssociation", results = { @Result(name = "findUserAssociation", 
			type = "fastJson",params = { "includeProperties","id,name,station"}) })
	public String findUserAssociation() {
		
		List<Customers> list=facadeService.getDecidedzoneService().findUserAssociation(getModel().getId());
		
		push(list);
		return "findUserAssociation";
	}
	//修改关联客户findNoAssociation
	@Action(value = "decidedzoneAction_assigncustomerstodecidedzone", results = {
			@Result(name = "assigncustomers", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String assigncustomerstodecidedzone() {
		String[] cid = getRequset().getParameterValues("customerIds");
		facadeService.getDecidedzoneService().assigncustomerstodecidedzone(getModel().getId(),cid);
		return "assigncustomers";
	}
//
//	@Action(value = "subareaAction_findAllSubarea", results = {@Result(name = "findAllSubarea", type = "fastJson",
//			params = { "includeProperties","sid,addresskey,position" }) })
//	public String findAllSubarea() {
//		List<Subarea> list=facadeService.getSubareaService().findAllSubarea();
//		push(list);
//		return "findAllSubarea";
//	}
//
//	// //regionAction_ajaxPostID
//	@Action(value = "subareaAction_ajaxRegionList", results = {
//			@Result(name = "ajaxRegionList", type = "fastJson", params = { "includeProperties", "id,name" }) })
//	public String ajaxRegionList() {
//		String parameter = getParameter("q");
//		List<Region> list = null;
//		if (StringUtils.isNotBlank(parameter)) {
//			list = facadeService.getRegionService().findByParame(parameter);
//		} else {
//
//			list = facadeService.getRegionService().findByParame();
//		}
//		push(list);
//		return "ajaxRegionList";
//	}
//
//	
//	
//	
	//复杂条件的抽取
	private Specification<Decidedzone> getSpecificationSubarea() {
		Specification<Decidedzone> spec=new Specification<Decidedzone>() {
			public Predicate toPredicate(Root<Decidedzone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pList=new ArrayList<>();
				//多表Decidedzone
				if (StringUtils.isNotBlank(getModel().getId())) {
					Predicate p1=cb.like(root.get("id").as(String.class), "%"+getModel().getId()+"%");
					pList.add(p1);
				}
				//staff表单表左外连接
				if (getModel().getStaff()!=null) {
					Join<Decidedzone, Staff> Staffjoin=root.join(root.getModel().getSingularAttribute("staff", Staff.class),JoinType.LEFT);
					if (StringUtils.isNotBlank(getModel().getStaff().getId())) {
						Predicate p2=cb.like(Staffjoin.get("id").as(String.class),getModel().getStaff().getId());
						pList.add(p2);
					}
					
				}
				//单表左外连接 Decidedzone多表Decidedzone
				String isAddSubarea = getRequset().getParameter("isAddSubarea");
				if (StringUtils.isNotBlank(isAddSubarea)) {
					if ("1".equals(isAddSubarea)) {
						Predicate p3 = cb.isNotEmpty(root.get("subareas").as(Set.class));
						pList.add(p3);
					} else if ("0".equals(isAddSubarea)) {
						Predicate p4 = cb.isEmpty(root.get("subareas").as(Set.class));
						pList.add(p4);
					}
				}
				Predicate[] p=new Predicate[pList.size()];
				
				return cb.and(pList.toArray(p));
			}
		};
		return spec;
	}

	
 //decidedzoneAction_delDecidedzone作废
	@Action(value = "decidedzoneAction_delDecidedzone", results = { @Result(name = "delDecidedzone", type = "json") })
	public String delDecidedzone() {
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] sid = ids.split(",");
				for (String id : sid) {
					getModel().setId(id);
					facadeService.getDecidedzoneService().deltag(getModel());
				}
			}
			push(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}

		return "delDecidedzone";
	}
	//
	 //批量还原staffAction_backtag
	 @Action(value="decidedzoneAction_ajaxDecidedID"
	 ,results={@Result(name="ajaxDecidedID",type="json")})
	 public String ajaxDecidedID(){
		 Decidedzone exitzone=facadeService.getDecidedzoneService().findById(getModel().getId());
		 if (exitzone==null) {
			push(true);
		}else{
			push(false);
		}
		 return "ajaxDecidedID";
	 }
}
