package cn.ssh.bos.service.qp.city;

import java.util.List;

import cn.ssh.bos.domain.qp.City;

public interface ILoadCityService {

	List<City> load(Integer pid);

	String loadFromRedis(Integer pid);

}
