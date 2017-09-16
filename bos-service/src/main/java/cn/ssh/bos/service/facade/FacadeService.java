package cn.ssh.bos.service.facade;

import java.io.Serializable;

import cn.ssh.bos.service.anth.func.IFunctionService;
import cn.ssh.bos.service.anth.menu.IMenuService;
import cn.ssh.bos.service.anth.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import cn.ssh.bos.service.bc.decidedzoneService.IDecidedzoneService;
import cn.ssh.bos.service.bc.region.IRegionService;
import cn.ssh.bos.service.bc.staff.IStaffService;
import cn.ssh.bos.service.bc.standard.IStandardService;
import cn.ssh.bos.service.bc.subarea.ISubareaService;
import cn.ssh.bos.service.qp.city.ILoadCityService;
import cn.ssh.bos.service.qp.noticebill.INoticeBillService;
import cn.ssh.bos.service.user.IUserService;

@Service("facadeService")
public class FacadeService {
	
	@Autowired
	private IUserService userService;
	
	public IUserService getUserService(){
		return userService;
	}
	
	/**
	 * Redis模板
	 */
	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	public RedisTemplate<Serializable, Serializable> getRedisTemplate(){
		return redisTemplate;
	}
	
	/**
	 * MQ模板
	 */
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	public JmsTemplate getJmsTemplate(){
		return jmsTemplate;
	}
	
	@Autowired
	private IStandardService standardService; 
	public IStandardService getStandardService() {
		// TODO Auto-generated method stub
		return standardService;
	}
	
	@Autowired
	private IStaffService staffService;
	public IStaffService getStaffService() {
		return staffService;
	}
	@Autowired
	private IRegionService regionService;

	public IRegionService getRegionService() {
		return regionService;
	}
	@Autowired
	private ISubareaService subareaService;
	public ISubareaService getSubareaService() {
		// TODO Auto-generated method stub
		return subareaService;
	}
	
	@Autowired
	private IDecidedzoneService decidedzoneService;
	public IDecidedzoneService getDecidedzoneService() {
		// TODO Auto-generated method stub
		return decidedzoneService;
	}
	@Autowired
	private ILoadCityService loadCityService;
	public ILoadCityService getLoadCityService() {
		// TODO Auto-generated method stub
		return loadCityService;
	}
	@Autowired
	private INoticeBillService noticeBillService;
	public INoticeBillService getNoticeBillService() {
		// TODO Auto-generated method stub
		return noticeBillService;
	}
	@Autowired
	private IFunctionService functionService;
	public IFunctionService getFunctionService() {
		return functionService;
	}

	@Autowired
	private IMenuService menuService;
	public IMenuService getMenuService() {
		return menuService;
	}

	@Autowired
	private IRoleService roleServie;
	public IRoleService getRoleService() {
		return roleServie;
	}
}
