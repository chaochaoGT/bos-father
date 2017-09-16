package cn.ssh.bos.service.bc.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.IStaffDao;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.bc.Standard;

@Service("staffService")
@Transactional
public class StaffService implements IStaffService {
	@Autowired
	private IStaffDao staffDao;
	
	@Override
	public void save(Staff staff) {
		// TODO Auto-generated method stub
		staffDao.save(staff);
	}

	@Override
	public Page<Staff> findPageDate(Pageable pageable) {
		// TODO Auto-generated method stub
//		Hibernate.initialize(Decidedzone.class);
		return staffDao.findAll(pageable);
	}

	@Override
	public void deltag(String id) {
		// TODO Auto-generated method stub
		staffDao.updateStaff(id);
	}

	@Override
	public void backtag(String id) {
		// TODO Auto-generated method stub
		
		staffDao.updateBacktag(id);
	}

	@Override
	public Staff findBytelephone(String telephone) {
		// TODO Auto-generated method stub
		return staffDao.findBytelephone(telephone);
	}

	@Override
	public Page<Staff> findPageDate(Specification<Staff> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return staffDao.findAll(spec, pageable);
	}

	@Override
	public List<Staff> findAll() {
		// TODO Auto-generated method stub
		return staffDao.findAllStaff();
	}

}
