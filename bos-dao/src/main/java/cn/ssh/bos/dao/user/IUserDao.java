package cn.ssh.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.ssh.bos.domain.user.User;

public interface IUserDao extends JpaRepository<User, Integer> ,JpaSpecificationExecutor<User>{

	
	//1.单表方法名查询
//	public User findByEmailAndPassword(String email, String password);
	
	//2.命名查询
//	public User login2(String email, String password);
	
	//3.JPQL语句查询
//	@Query("from User where email=?1 and password=?2")
//	public User login(String email,String password);
	
	//4.纯SQL语句查询
//	@Query(nativeQuery=true,value="select * from t_user where email=?1 and password=?2")
//	public User login(String email,String password);
	
	//5.占位符
	@Query("from User where email=:email and password=:pwd")
	public User login(@Param("email")String email,@Param("pwd")String password);
	
	public User findByTelephone(String telephone);

	@Modifying
	@Query("update User set password=?1 where telephone=?2")
	public void updateUser(String password,String telephone);

	@Query("from User where email=?1 ")
	User findByEmial(String username);

	@Query("from User where id=?1 ")
	User findById(int id);
}
