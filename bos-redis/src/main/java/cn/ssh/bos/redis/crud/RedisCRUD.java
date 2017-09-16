package cn.ssh.bos.redis.crud;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
@Repository("redisCRUD")
public class RedisCRUD  implements IRedisCRUD {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	@Override
	public String getJsonStringCitysByPid(String key) {
		// TODO Auto-generated method stub
		return (String) redisTemplate.opsForValue().get(key);
	}

	@Override
	public void putJsonStringCityToRedis(String key, String citys) {
		// TODO Auto-generated method stub
		redisTemplate.opsForValue().append(key, citys);
	}

	@Override
	public void delJsonStringCityByPid(String key) {
		// TODO Auto-generated method stub
		redisTemplate.delete(key);
	}

	@Override
	public String GetJSONStringFromRedis(String keyId) {
		return (String) redisTemplate.opsForValue().get(keyId);
	}

	@Override
	public void writeJSONStringToRedis(String keyId, String jsonvalue) {
		redisTemplate.opsForValue().append(keyId,jsonvalue);
	}

}
