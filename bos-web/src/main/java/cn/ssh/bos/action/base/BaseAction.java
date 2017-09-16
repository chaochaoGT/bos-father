package cn.ssh.bos.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.ssh.bos.util.fileupdown.DownLoadUtils;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	
	private T model;
	
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	//构造实例化model
	public BaseAction(){
		//子类获取父类的通用类型
		Type superclass = this.getClass().getGenericSuperclass();
		//通用类型转化为通用参数类型
		 ParameterizedType parameterizedType= (ParameterizedType) superclass;
		//获取泛型类的类型
		 Type modelType = parameterizedType.getActualTypeArguments()[0];
		//泛型类转换成字节码类型
		 Class modelClass=(Class) modelType;
		try {
			//newInter 实例化泛型类
			model=(T) modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ServletContext getContext(){
		return ServletActionContext.getServletContext();
	}
	//request
	public HttpServletRequest getRequset(){
		return ServletActionContext.getRequest();
	}
	public String getParameter(String name){
		return ServletActionContext.getRequest().getParameter(name);
	}
	public void setParameter(String name,Object o){
		 ServletActionContext.getRequest().setAttribute(name,o);
	}
	
	//response
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	//session 存值
	public Object getSessionAttribute(String name){
		return ServletActionContext.getRequest().getSession().getAttribute(name);
	}
	
	//session取值
	public void setSessionAttribute(String name,Object o){
		ServletActionContext.getRequest().getSession().setAttribute(name,o);
	}
	
	//session移除
	public void removeSessionAttribute(String name){
		 ServletActionContext.getRequest().getSession().removeAttribute(name);
	}
	
	//root值栈的    push
	public void push(Object o){
		ActionContext.getContext().getValueStack().push(o);
	}
	
	//set
	public void set(String key, Object o){
		ActionContext.getContext().getValueStack().set(key, o);
	}
	
	//map 值栈 的  put 
	public void put (String key,Object o ){
		ActionContext.getContext().getValueStack().getContext().put(key, o);
	}
	
	//json序列化
	
	//文件下载 两个头  一个流
	public void doweloadFile(String filename, String path){
		HttpServletResponse response = getResponse();
		
		try {
			//设置下载文件的形式和文件名头
			response.setHeader("Content-Disposition", "attachment;filename="+DownLoadUtils.getAttachmentFileName(filename, 
					getRequset().getHeader("user-agent")));
			//设置下载文件类型头
			 ServletContext context = ServletActionContext.getServletContext();
			response.setContentType(context.getMimeType(filename));
			//获取响应流
			ServletOutputStream out= response.getOutputStream();
			//读取服务器的文件到内存
			InputStream stream =new FileInputStream(path);
			
			int len ;
			byte[] arrs=new byte[1024*8];
			while((len=stream.read(arrs))!=-1){
				//下载从内村写出到浏览器
				out.write(arrs, 0, len);
			}
			stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	//分页数据
	protected int page=1;
	protected int rows=3;

	public void setRows(int rows) {
		this.rows = rows;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	
	/**
	 * 封装页面请求数据
	 * @return Pageable 
	 */
	public Pageable getPageable(){
		return new PageRequest(page-1, rows);
	}
	/**
	 * 封装业务层查询出来的数据集合
	 * 赋值给pagedate
	 */
	private Page<T> pagedate;
	public void setPagedate(Page<T> pagedate){
		this.pagedate=pagedate;
	}
	/**
	 *分页封装Map结果集
	 * @return PageMap
	 */
	public Object getPageMap(){
		Map<Serializable, Object> map = new HashMap<>();
		map.put("total", pagedate.getTotalElements());
		map.put("rows", pagedate.getContent());
		return map; 
	}
	
	//文件上传
	protected File upload;
	public void setUpload(File upload) {
		this.upload = upload;
	}
	/**
	 * 简记码
	 * @param strings
	 * @return
	 */
	protected String getHeadFromArray(String[] strings) {
		if (strings == null || strings.length == 0) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String s : strings) {
				sb.append(s);
			}
			return sb.toString();
		}

	}
}


