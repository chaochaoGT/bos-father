package cn.ssh.bos.action.bc;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import cn.ssh.bos.util.fileupdown.DownLoadUtils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.ssh.bos.action.base.BaseAction;
import cn.ssh.bos.domain.bc.Region;
import cn.ssh.bos.service.facade.FacadeService;
import cn.ssh.bos.uitl.pinyin.PinYin4jUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Controller("regionAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/region")
public class RegionAction extends BaseAction<Region> {
	
	@Autowired
	private FacadeService facadeService;

	@Autowired
	private DataSource dataSource;

	
	//staffAction_save增加区域信息
	@Action(value="regionAction_save" ,results={@Result(name="save",location="/WEB-INF/pages/base/region.jsp")})
	public String save(){
		try {
			facadeService.getRegionService().save(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "save";
	}
	
//批量删除regionAction_Delete
	@Action(value="regionAction_Delete" ,results={@Result(name="Delete",type="json")})
	public String backtag(){
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] sid = ids.split(",");
				for (String id : sid) {
					facadeService.getRegionService().deleteRegion(id);
				}
			}
			push(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}
		return "Delete";
	}
			

	
	@Action(value="regionAction_ajaxPostcode" ,results={@Result(name="ajaxPostcode",type="json")})
	public String ajaxPostcode(){
		Region exitRegion=facadeService.getRegionService().findByPostcode(getModel().getPostcode());
		if (exitRegion==null) {
			push(true);
		}else{
			push(false);
		}
		return "ajaxPostcode";
	}
	
	//regionAction_ajaxPostID
	@Action(value="regionAction_ajaxPostID" ,results={@Result(name="ajaxPostID",type="json")})
	public String ajaxPostID(){
		Region exitRegion=facadeService.getRegionService().findById(getModel().getId());
		if (exitRegion==null) {
			push(true);
		}else{
			push(false);
		}
		return "ajaxPostID";
	}
	//上传文件regionAction_oneclickupload
	@Action(value="regionAction_oneclickupload" ,results={@Result(name="oneclickupload"
			,type="fastJson")})
	public String oneclickupload(){
		try {
			HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheet = workbook.getSheetAt(0);
			List<Region> list =new ArrayList<>();
			for (Row r : sheet) {
				int rowNum = r.getRowNum();
				if (rowNum==0) {
					continue;
				}
				Region region = new Region();
				region.setId(r.getCell(0).getStringCellValue());
				String province=r.getCell(1).getStringCellValue();
				region.setProvince(province);
				String city=r.getCell(2).getStringCellValue();
				region.setCity(city);
				String district=r.getCell(3).getStringCellValue();
				region.setDistrict(district);
				region.setPostcode(r.getCell(4).getStringCellValue());
				// 城市简码 简码
				city = city.substring(0, city.length() - 1);
				region.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));
				
				//简记码
				// 江苏南京鼓楼
				province = province.substring(0, province.length() - 1);
				district = district.substring(0, district.length() - 1);
				String[] strings;
				if (province.equalsIgnoreCase(city)) {
					// 直辖市
					strings = PinYin4jUtils.getHeadByString(province + district);
				} else {
					// 非直辖市
					strings = PinYin4jUtils.getHeadByString(province + city + district);
				}
				// 省市区关键字首字母 拼接字符串 遍历
				String shortcode = getHeadFromArray(strings);
				region.setShortcode(shortcode); // 省市区 每一个中文字首字母大写组成
				
				list.add(region);
			}
			facadeService.getRegionService().save(list);
			push(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			push(false);
		}
		return "oneclickupload";
	}
	
	//条件和进行重载分页查询regionAction_pageDate
//	@Action(value="regionAction_pageDate")
//	public String pageDate(){
//		Page<Region> pagedate=facadeService.getRegionService().findPageDate(getPageable());
//			setPagedate(pagedate);
//		return "pageDate";
//	}

	@Action(value = "regionAction_pageDate")
	public String pageDate() {
		String string = facadeService.getRegionService().pageQueryByRedis(getPageable());
		try {
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().println(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;// 分页查询结果集名称
	}

	//查询所有收货标准
	@Action(value = "regionAction_doExportPDF")
	public String doExportPDF() {
		try {
			System.out.println("下载PDF===================");
			List<Region> regions = facadeService.getRegionService().findAll();
			Document document = new Document();
			HttpServletResponse response = getResponse();
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			// pdf 可以设置密码
			writer.setEncryption("itcast".getBytes(), "heima04".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
			// 浏览器下载 ...两个头
			String filename = new Date(System.currentTimeMillis()) + "_区域数据.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));// mime 类型
			response.setHeader("Content-Disposition", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename,
					ServletActionContext.getRequest().getHeader("user-agent")));
			// 打开文档
			document.open();
			Table table = new Table(5, regions.size() + 1);// 5列 行号 0 开始
			// table.setBorderWidth(1f);
			// table.setAlignment(1);// // 其中1为居中对齐，2为右对齐，3为左对齐
			// table.setBorder(1); // 边框
			// table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
			// table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
			// 设置表格字体
			BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
			// 表头
			table.addCell(buildCell("省", font));
			table.addCell(buildCell("市", font));
			table.addCell(buildCell("区", font));
			table.addCell(buildCell("邮政编码", font));
			table.addCell(buildCell("简码", font));

			// 表格数据
			for (Region region : regions) {
				table.addCell(buildCell(region.getProvince(), font));
				table.addCell(buildCell(region.getCity(), font));
				table.addCell(buildCell(region.getDistrict(), font));
				table.addCell(buildCell(region.getPostcode(), font));
				table.addCell(buildCell(region.getShortcode(), font));
			}
			// 向文档添加表格
			document.add(table);
						document.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	//查询所有收货标准
	@Action(value = "regionAction_doExportPDF1")
	public String doExportPDF1() {
		try {
			//1准备报表文件路径
			String path = ServletActionContext.getServletContext().getRealPath("/jr/report1.jrxml");
			//2.报表的赋值
			Map<String,Object> map =new HashMap<>();
			map.put("author","wangchao");
			//编译文件
			// 3: 编译该文件 JasperCompilerManager
			JasperReport report = JasperCompileManager.compileReport(path);
			// 4: JapserPrint = JasperFillManager.fillReport(report,map,connection)
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, dataSource.getConnection());
			// 5: 下载 准备一个流 两个头
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream outputStream = response.getOutputStream();
			String filename = "工作单报表.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename,
					ServletActionContext.getRequest().getHeader("user-agent")));
			// 6: JapdfExport   定义报表输出源
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// 7: 导出
			exporter.exportReport();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;

	}

	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		Cell cell = new Cell(phrase);
		// 设置垂直居中
		// cell.setVerticalAlignment(1);
		// 设置水平居中
		// cell.setHorizontalAlignment(1);
		return cell;
}
//	//staffAction_deltag作废
//	//批量作废收货标准
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
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
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
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "backtag";
//		}
		
}
