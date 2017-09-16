package cn.ssh.bos.dao.qp;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.ssh.bos.domain.qp.City;

public interface ILoadCityDao extends JpaRepository<City, Serializable>,JpaSpecificationExecutor<City>{

	@Query("from City where pid=?")
	List<City> findCitys(Integer pid);

}
