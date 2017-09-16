package cn.ssh.bos.service.bc.region;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.ssh.bos.domain.bc.Region;

public interface IRegionService {

	void save(List<Region> list);

	Page<Region> findPageDate(Pageable pageable);

	void save(Region model);

	Region findByPostcode(String postcode);

	Region findById(String id);

	List<Region> findByParame();

	List<Region> findByParame(String parameter);

	void deleteRegion(String id);


	List<Region> findAll();

	String pageQueryByRedis(Pageable pageable);
}
