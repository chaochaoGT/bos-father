package cn.ssh.bos.service.user;

import java.util.List;
import java.util.Set;

import cn.ssh.bos.domain.auth.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.user.IUserDao;
import cn.ssh.bos.domain.user.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		userDao.save(user);
	}

	@Override
	public Page<User> findPageDate(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	public void deleteUser(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			String[] sid = ids.split(",");
			for (String id : sid) {
				User user=userDao.findById(Integer.parseInt(id));
				user.getRoles().clear();
				user.getNoticeBills().clear();
				userDao.delete(Integer.parseInt(id));
			}
		}
	}

	@Override
	public User ajaxUserEmail(User model) {
		return userDao.findByEmial(model.getEmail());
	}

	@Override
	public User ajaxTelephone(User model) {
		return userDao.findByTelephone(model.getTelephone());
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		userDao.delete(user);
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return userDao.findOne(id);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

	@Override
	public User login(String email, String password) {
		// TODO Auto-generated method stub
//		return userDao.findByEmailAndPassword(email,password);
		return userDao.login(email,password);
	}

	@Override
	public User findByTelephone(String telephone) {
		// TODO Auto-generated method stub
		return userDao.findByTelephone(telephone);
	}

	@Override
	public void updateUser(User model) {
		// TODO Auto-generated method stub
		userDao.updateUser(model.getPassword(),model.getTelephone());
	}

	@Override
	public User findByEmial(String username) {
		return userDao.findByEmial(username);
	}

	@Override
	public void save(User model, String[] rids) {
		userDao.saveAndFlush(model);
		if (rids != null&&rids.length>0) {
			Set<Role> functions = model.getRoles();
			for (String fid:rids) {
				Role func=new Role();
				func.setId(fid);
				functions.add(func);
			}
		}
	}
}
