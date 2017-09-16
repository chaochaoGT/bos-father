package cn.ssh.bos.dao.bc;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.ssh.bos.domain.bc.Standard;

public interface IStandardDao extends JpaRepository<Standard, Serializable>{
	
	@Modifying
	@Query("update Standard set deltag=0 where id=?1")
	public void updateDeltag(int id);

	@Modifying
	@Query("update Standard set deltag=1 where id=?1")
	public void backtag(int id);

	@Query("from Standard where deltag=1")
	public List<Standard> findAllByUse();

}
