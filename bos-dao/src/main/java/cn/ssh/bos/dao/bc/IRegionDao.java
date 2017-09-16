package cn.ssh.bos.dao.bc;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.ssh.bos.domain.bc.Region;

public interface IRegionDao extends JpaRepository<Region, Serializable> ,JpaSpecificationExecutor<Region>{

	Region findByPostcode(String postcode);

	Region findById(String id);
	
	@Modifying
	@Query("from Region where province like ?1 or city like ?1 or district like ?1")
	List<Region> findRegion(String string);

	@Query("from Region where province = ?1 and city =?2 and district =?3")
	Region findByCitys(String province, String city, String district);
}
