package cn.ssh.bos.dao.bc;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.ssh.bos.domain.bc.Decidedzone;

public interface IDecidedzoneDao extends JpaRepository<Decidedzone, Serializable>, JpaSpecificationExecutor<Decidedzone> {

	Decidedzone findById(String id);
	
	

}
