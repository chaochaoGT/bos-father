package cn.ssh.bos.service.bc.decidedzoneService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.crm.Customers;

public interface IDecidedzoneService {

	void save(String[] sid, Decidedzone model);

	Page<Decidedzone> findPageDate(Pageable pageable);

	Page<Decidedzone> findPageDate(Specification<Decidedzone> spec, Pageable pageable);

	void deltag(Decidedzone decidedzone);

	Decidedzone findById(String id);

	List<Customers> findUserAssociation(String id);

	List<Customers> findNoAssociation();

	void assigncustomerstodecidedzone(String id, String[] cids);

}
