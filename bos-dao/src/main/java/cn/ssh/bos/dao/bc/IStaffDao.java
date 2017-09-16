package cn.ssh.bos.dao.bc;
import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.ssh.bos.domain.bc.Staff;

public interface IStaffDao extends JpaRepository<Staff, Serializable>,JpaSpecificationExecutor<Staff> {

	@Modifying
	@Query("update Staff set deltag=0 where id=?1")
	public void updateStaff(String id);

	@Modifying
	@Query("update Staff set deltag=1 where id=?1")
	public void updateBacktag(String id);

	public Staff findBytelephone(String telephone);

	@Query("from Staff where deltag=1")
	public List<Staff> findAllStaff();


}
