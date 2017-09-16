package cn.ssh.bos.service.qp.city;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.ssh.bos.dao.qp.ILoadCityDao;
import cn.ssh.bos.domain.qp.City;
import cn.ssh.bos.redis.crud.IRedisCRUD;
@Service("loadCityService")
@Transactional
public class LoadCityServciceImpl implements ILoadCityService {
	
	@Autowired
	private ILoadCityDao loadCityDao ;
	@Autowired
	private IRedisCRUD redisCRUD; 
	//版本一
	@Override
	public List<City> load(Integer pid) {
		// TODO Auto-generated method stub
		return loadCityDao.findCitys(pid);
	}
	
	//版本二  redis
	@Override
	public String loadFromRedis(Integer pid) {
		// TODO Auto-generated method stub
		String citys =redisCRUD.getJsonStringCitysByPid(Integer.toString(pid));
		if (StringUtils.isBlank(citys)) {
			List<City> list = load(pid);
			citys = JSON.toJSONString(list);
			redisCRUD.putJsonStringCityToRedis(Integer.toString(pid),citys);
		}
		return citys;
	}
	
	//版本三 redis+AOP

}
