package cn.ssh.bos.service.bc.subarea;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Subarea;

public interface ISubareaService {

	Page<Subarea> findPageDate(Pageable pageable);


	void save(Subarea model);


	void save(List<Subarea> list);


	void deltag(String id);


	Page<Subarea> findPageDate(Specification<Subarea> spec, Pageable pageable);


	List<Subarea> findPageDate(Specification<Subarea> spec);


	List<Subarea> findAllSubarea();


	List<Subarea> findAllSubarea(Decidedzone decidedzone);








}
