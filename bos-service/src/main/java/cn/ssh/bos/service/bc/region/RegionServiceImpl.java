package cn.ssh.bos.service.bc.region;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ssh.bos.redis.crud.IRedisCRUD;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.IRegionDao;
import cn.ssh.bos.domain.bc.Region;

@Service("regionService")
@Transactional
public class RegionServiceImpl implements IRegionService {
	@Autowired
	private IRegionDao regionDao;

	@Autowired
	private IRedisCRUD redisCRUD;
	
	@Override
	public void save(List<Region> list) {
		// TODO Auto-generated method stub
		regionDao.save(list);
	}

	@Override
	public Page<Region> findPageDate(Pageable pageable) {
		// TODO Auto-generated method stub
		return regionDao.findAll(pageable);
	}

	@Override
	public void save(Region model) {
		// TODO Auto-generated method stub
		regionDao.save(model);
	}

	@Override
	public Region findByPostcode(String postcode) {
		// TODO Auto-generated method stub
		return regionDao.findByPostcode(postcode);
	}

	@Override
	public Region findById(String id) {
		// TODO Auto-generated method stub
		return regionDao.findById(id);
	}
	@Override
	public List<Region> findByParame(String parameter) {
		// TODO Auto-generated method stub
		return regionDao.findRegion("%"+parameter+"%");
	}

	@Override
	public List<Region> findByParame() {
		// TODO Auto-generated method stub
		return regionDao.findAll();
	}

	@Override
	public void deleteRegion(String id) {
		// TODO Auto-generated method stub
		regionDao.delete(id);
	}

	@Override
	public List<Region> findAll() {
		return regionDao.findAll();
	}

	// @Cacheable(key = "#pageRequest.pageNumber+'_'+#pageRequest.pageSize", value = "region_list")
	@Override
	public String pageQueryByRedis(Pageable pageRequest) {
		// 区域大量数据 第一次查询数据库 存放缓冲服务器 redis 第二次查询 从redis取 避免频繁和数据库交互!
		// 如果 数据库发生更新 及时更新缓存redis
		int pageNumber = pageRequest.getPageNumber();// 获取当前查询的页码
		int pageSize = pageRequest.getPageSize();// 获取当前查询每页记录数
		String keyId = pageNumber + "_" + pageSize;// 因为是分页查询 ,所以存在redis服务器的key需要唯一!
		// 通过key查询对应的分页数据 所以采用1_10 2_10 页码_每页记录数作为唯一标识 存储在redis服务器上
		String jsonvalue = redisCRUD.GetJSONStringFromRedis(keyId);
		if (StringUtils.isBlank(jsonvalue)) {
			// 第一次查询数据库,将数据库数据序列化Json格式的数据存储到redis 服务器上
			Page<Region> pageData = regionDao.findAll(pageRequest);// 查询数据库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", pageData.getTotalElements());
			map.put("rows", pageData.getContent());
			jsonvalue = JSON.toJSONString(map);// 满足easyui需要分页数据的json格式
			// jsonvalue = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);// 满足easyui需要分页数据的json格式
			redisCRUD.writeJSONStringToRedis(keyId, jsonvalue);// 将分页的json格式数据存储到redis服务器上
		}
		return jsonvalue;
	}

}
