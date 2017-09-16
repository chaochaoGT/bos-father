package cn.ssh.bos.service.user;

import java.util.List;

import cn.ssh.bos.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
	

	public void delete(User user);

	public User findUserById(Integer id);

	public List<User> findAll();
	
	//登陆
	public User login(String email,String password);

	public User findByTelephone(String telephone);

	public void updateUser(User model);

	User findByEmial(String username);

	void save(User model, String[] rids);

	void save(User exitUser1);

	Page<User> findPageDate(Pageable pageable);

	void deleteUser(String ids);

	User ajaxUserEmail(User model);

	User ajaxTelephone(User model);
}
