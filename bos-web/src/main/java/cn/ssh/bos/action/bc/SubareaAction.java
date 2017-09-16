package cn.ssh.bos.action.bc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import cn.ssh.bos.domain.bc.Region;
import cn.ssh.bos.domain.bc.Subarea;
import cn.ssh.bos.service.facade.FacadeService;
import cn.ssh.bos.util.fileupdown.DownLoadUtils;

@Controller("subareaAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/subarea")
public class SubareaAction extends BaseAction<Subarea> {

	@Autowired
	private FacadeService facadeService;

	// //staffAction_save增加分区信息
	@Action(value = "subareaAction_save", results = {
			@Result(name = "save", location = "/WEB-INF/pages/base/subarea.jsp") })
	public String save() {
		try {
			facadeService.getSubareaService().save(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}

	//修改分区
	@Action(value = "subareaAction_update", results = { @Result(name = "update", 
			type = "fastJson") })
	public String update() {
		try {
			Region region = new Region();
			String regionid = getParameter("region[id]");
			region.setId(regionid);
			Subarea subarea = getModel();
			subarea.setRegion(region);
			facadeService.getSubareaService().save(subarea);
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "update";
	}
	
	//查询未定的区域
	@Action(value = "subareaAction_findAllSubarea", results = {@Result(name = "findAllSubarea", type = "fastJson",
			params = { "includeProperties","sid,addresskey,position" }) })
	public String findAllSubarea() {
		List<Subarea> list=facadeService.getSubareaService().findAllSubarea();
		push(list);
		return "findAllSubarea";
	}
	
	//查询已定的区域
	@Action(value = "subareaAction_findUsedAllSubarea", results = {
			@Result(name = "findUsedAllSubarea", type = "fastJson", params = { "includeProperties",
					"sid,addresskey,position" }) })
	public String findUsedAllSubarea() {
		List<Subarea> list = facadeService.getSubareaService().findAllSubarea(getModel().getDecidedzone());
		push(list);
		return "findUsedAllSubarea";
	}

	// //regionAction_ajaxPostID
	@Action(value = "subareaAction_ajaxRegionList", results = {
			@Result(name = "ajaxRegionList", type = "fastJson", params = { "includeProperties", "id,name" }) })
	public String ajaxRegionList() {
		String parameter = getParameter("q");
		List<Region> list = null;
		if (StringUtils.isNotBlank(parameter)) {
			list = facadeService.getRegionService().findByParame(parameter);
		} else {

			list = facadeService.getRegionService().findByParame();
		}
		push(list);
		return "ajaxRegionList";
	}

	// //上传文件subareaAction_oneclickupload
	@Action(value = "subareaAction_oneclickupload", results = { @Result(name = "oneclickupload", type = "fastJson") })
	public String oneclickupload() {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheet = workbook.getSheetAt(0);
			List<Subarea> list = new ArrayList<>();
			for (Row r : sheet) {
				int rowNum = r.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				Subarea subarea = new Subarea();
				Region region = new Region();
				// 区域编码 关键字 起始号 结束号 单双号 位置信息
				region.setId(r.getCell(1).getStringCellValue());
				subarea.setRegion(region);

				subarea.setAddresskey(r.getCell(2).getStringCellValue());
				subarea.setStartnum(r.getCell(3).getStringCellValue());
				subarea.setEndnum(r.getCell(4).getStringCellValue());
				String single = r.getCell(5).getStringCellValue();
				subarea.setSingle(Character.valueOf(single.charAt(0)));
				subarea.setPosition(r.getCell(6).getStringCellValue());
				list.add(subarea);
			}
			facadeService.getSubareaService().save(list);
			push(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}
		return "oneclickupload";
	}

	//文件下载
	@Action(value = "subareaAction_download")
	public String download() {
		
		try {
			//获取要下载的subarea;
			List<Subarea> list=facadeService.getSubareaService().findPageDate(getSpecificationSubarea());
			//创建表格
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("分区数据");
			// excel标题
			HSSFRow first = sheet.createRow(0);
			first.createCell(0).setCellValue("分区编号");
			first.createCell(1).setCellValue("省");
			first.createCell(2).setCellValue("市");
			first.createCell(3).setCellValue("区");
			first.createCell(4).setCellValue("关键字");
			first.createCell(5).setCellValue("起始号");
			first.createCell(6).setCellValue("终止号");
			first.createCell(7).setCellValue("单双号");
			first.createCell(8).setCellValue("位置");
			// 数据体
			if (list!=null&&list.size()>0) {
				for (Subarea s : list) {
					// 循环一次创建一行
					int lastRowNum = sheet.getLastRowNum();// 获取当前excel最后一行行号
					HSSFRow row = sheet.createRow(lastRowNum + 1);
					row.createCell(0).setCellValue(s.getId());
					row.createCell(1).setCellValue(s.getRegion().getProvince());
					row.createCell(2).setCellValue(s.getRegion().getCity());
					row.createCell(3).setCellValue(s.getRegion().getDistrict());
					row.createCell(4).setCellValue(s.getAddresskey());
					row.createCell(5).setCellValue(s.getStartnum());
					row.createCell(6).setCellValue(s.getEndnum());
					row.createCell(7).setCellValue(s.getSingle() + "");
					row.createCell(8).setCellValue(s.getPosition());
				}
				
			}
			//下载
			String filename="分区数据.xls";
			HttpServletResponse response = getResponse();
			response.setHeader("Content-Disposition", "attachment;filename="+
					DownLoadUtils.getAttachmentFileName(filename, getRequset().getHeader("user-agent")));
			response.setContentType(getContext().getMimeType(filename));
			workbook.write(response.getOutputStream());
			push(true);
			return NONE;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
			throw new RuntimeException(e);
		}
	}
	// 条件和进行重载分页查询regionAction_pageDate
	@Action(value = "subareaAction_pageDate")
	public String pageDate() {
		Specification<Subarea> spec = getSpecificationSubarea();
		Page<Subarea> pagedate = facadeService.getSubareaService().findPageDate(spec,getPageable());
		setPagedate(pagedate);
		return "pageDate";
	}
	//复杂条件的抽取
	private Specification<Subarea> getSpecificationSubarea() {
		Specification<Subarea> spec=new Specification<Subarea>() {
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pList=new ArrayList<>();
				//多表
				if (StringUtils.isNotBlank(getModel().getAddresskey())) {
					Predicate p1=cb.like(root.get("addresskey").as(String.class), "%"+getModel().getAddresskey()+"%");
					pList.add(p1);
				}
				//单表左外连接 region表
				if (getModel().getRegion()!=null) {
					Join<Subarea, Region> regjoin=root.join(root.getModel().getSingularAttribute("region", Region.class),JoinType.LEFT);
					Region region = getModel().getRegion();
					if (StringUtils.isNotBlank(region.getProvince())) {
						Predicate p2=cb.like(regjoin.get("province").as(String.class), "%"+region.getProvince()+"%");
						pList.add(p2);
					}
					if (StringUtils.isNotBlank(region.getCity())) {
						Predicate p3=cb.like(regjoin.get("city").as(String.class), "%"+region.getCity()+"%");
						pList.add(p3);
					}
					if (StringUtils.isNotBlank(region.getDistrict())) {
						Predicate p4=cb.like(regjoin.get("district").as(String.class), "%"+region.getDistrict()+"%");
						pList.add(p4);
					}
				}
				//单表左外连接 Decidedzone表Decidedzone
				if (getModel().getDecidedzone()!=null&&StringUtils.isNotBlank(getModel().getDecidedzone().getId())) {
					Predicate p5=cb.equal(root.get("decidedzone").as(Decidedzone.class), getModel().getDecidedzone());
					pList.add(p5);
				}
				Predicate[] p=new Predicate[pList.size()];
				
				return cb.and(pList.toArray(p));
			}
		};
		return spec;
	}

	//
	// //subareaAction_Delete作废
	// 批量删除subarea
	@Action(value = "subareaAction_Delete", results = { @Result(name = "deltag", type = "json") })
	public String deltag() {
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] sid = ids.split(",");
				for (String id : sid) {
					facadeService.getSubareaService().deltag(id);
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
	//
	// //批量还原staffAction_backtag
	// @Action(value="staffAction_backtag"
	// ,results={@Result(name="backtag",type="json")})
	// public String backtag(){
	// try {
	// String ids = getParameter("ids");
	// if (StringUtils.isNotBlank(ids)) {
	// String[] sid = ids.split(",");
	// for (String id : sid) {
	// facadeService.getStaffService().backtag(id);
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return "backtag";
	// }

}
