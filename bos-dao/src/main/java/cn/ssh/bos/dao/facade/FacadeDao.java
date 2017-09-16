package cn.ssh.bos.dao.facade;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ssh.bos.dao.bc.IRegionDao;
import cn.ssh.bos.dao.bc.IStaffDao;
import cn.ssh.bos.dao.bc.IStandardDao;
import cn.ssh.bos.dao.bc.ISubareaDao;
import cn.ssh.bos.dao.user.IUserDao;



public class FacadeDao {
	
	@Autowired
	private IUserDao userDao;
	
	public IUserDao getUserDao(){
		return userDao;
	}
	

	
	
	
	@Autowired
	private IStandardDao standardDao; 
	public IStandardDao getStandardDao() {
		// TODO Auto-generated method stub
		return standardDao;
	}
	
	@Autowired
	private IStaffDao staffDao;
	public IStaffDao getStaffDao() {
		return staffDao;
	}
	@Autowired
	private IRegionDao regionDao;

	public IRegionDao getRegionDao() {
		return regionDao;
	}
	@Autowired
	private ISubareaDao subareaDao;
	public ISubareaDao getSubareaDao() {
		// TODO Auto-generated method stub
		return subareaDao;
	}
}
