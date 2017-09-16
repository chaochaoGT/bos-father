package cn.ssh.bos.redis.crud;

public interface IRedisCRUD {

	String getJsonStringCitysByPid(String key);

	void putJsonStringCityToRedis(String key, String citys);

	void delJsonStringCityByPid(String key);

	String GetJSONStringFromRedis(String keyId);

	void writeJSONStringToRedis(String keyId, String jsonvalue);
}
