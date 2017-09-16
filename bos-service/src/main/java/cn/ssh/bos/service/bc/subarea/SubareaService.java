package cn.ssh.bos.service.bc.subarea;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.ISubareaDao;
import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Subarea;
@Service("subareaService")
@Transactional
public class SubareaService implements ISubareaService {
	@Autowired
	private ISubareaDao subareaDao;
	@Override
	public Page<Subarea> findPageDate(Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Subarea> page = subareaDao.findAll(pageable);
		List<Subarea> list = page.getContent();
		for (Subarea subarea : list) {
			//get是不立刻查询而是先去一级缓存查找,没有在查
			//subarea.getRegion().getCitycode();	
			//立刻加载并发送sql查询
			Hibernate.initialize(subarea.getRegion());
		}
		return page;
	}
	
	@Override
	public void save(Subarea model) {
		// TODO Auto-generated method stub
		subareaDao.save(model);
	}

	@Override
	public void save(List<Subarea> list) {
		// TODO Auto-generated method stub
		subareaDao.save(list);
	}

	@Override
	public void deltag(String id) {
		// TODO Auto-generated method stub
		subareaDao.delete(id);
	}

	@Override
	public Page<Subarea> findPageDate(Specification<Subarea> spec, Pageable pageable) {
		Page<Subarea> page = subareaDao.findAll(spec, pageable);
		List<Subarea> list = page.getContent();
		for (Subarea subarea : list) {
			Hibernate.initialize(subarea.getRegion());
			//subarea.jsp页面不需要
			//Hibernate.initialize(subarea.getDecidedzone());
		}
		return page;
	}

	@Override
	public List<Subarea> findPageDate(Specification<Subarea> spec) {
		// TODO Auto-generated method stub
		List<Subarea> list = subareaDao.findAll(spec);
		for (Subarea subarea : list) {
			Hibernate.initialize(subarea.getRegion());
			//subarea.jsp页面不需要
			//Hibernate.initialize(subarea.getDecidedzone());
		}
		return list;
	}

	@Override
	public List<Subarea> findAllSubarea() {
		// TODO Auto-generated method stub
		return subareaDao.findAllSubarea();
	}

	@Override
	public List<Subarea> findAllSubarea(Decidedzone decidedzone) {
		
		return subareaDao.findByDecidedzone(decidedzone);
	}

	


}
