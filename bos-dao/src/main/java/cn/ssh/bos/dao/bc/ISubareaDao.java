package cn.ssh.bos.dao.bc;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Subarea;
@Repository("subareaDao")
public interface ISubareaDao extends JpaRepository<Subarea, Serializable>, JpaSpecificationExecutor<Subarea> {

	@Query("from Subarea where decidedzone is null")
	List<Subarea> findAllSubarea();

	
	@Modifying
	@Query("update Subarea set decidedzone=?1 where id=?2")
	void updateSubarea(Decidedzone model, String sub);

	@Query("from Subarea where decidedzone=?1")
	List<Subarea> findByDecidedzone(Decidedzone decidedzone);

	
	

	

}
