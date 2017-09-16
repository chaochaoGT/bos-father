package cn.ssh.bos.service.bc.standard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.IStandardDao;
import cn.ssh.bos.domain.bc.Standard;
@Service("standardService")
@Transactional
public class StandardServiceImpl implements IStandardService {

	@Autowired
	private IStandardDao standardDao;
	
	@Override
	public void save(Standard standard) {
		// TODO Auto-generated method stub
		standardDao.save(standard);
	}

	@Override
	public Page<Standard> findPageDate(Pageable pageRequest) {
		// TODO Auto-generated method stub
		return standardDao.findAll(pageRequest);
	}

	@Override
	public void deltag(int id) {
		// TODO Auto-generated method stub
		standardDao.updateDeltag(id);
	}

	@Override
	public void backtag(int id) {
		// TODO Auto-generated method stub
		standardDao.backtag(id);
	}

	@Override
	public List<Standard> findAll() {
		// TODO Auto-generated method stub
		return standardDao.findAllByUse();
	}

}
