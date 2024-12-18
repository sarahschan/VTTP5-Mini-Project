package sg.edu.nus.iss.mini_project.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.mini_project.constant.Constant;

@Repository
public class RedisRepo {
    
    @Autowired
    @Qualifier(Constant.REDIS_TEMPLATE)
    RedisTemplate<String, String> redisTemplate;

    public void saveValue(String redisKey, String hashKey, String value){
        redisTemplate.opsForHash().put(redisKey, hashKey, value);
    }

    public Object getValue(String redisKey, String hashKey){
        return redisTemplate.opsForHash().get(redisKey, hashKey);
    }

    public List<Object> getAllValues(String redisKey) {
        return redisTemplate.opsForHash().values(redisKey);
    }

    public long delete(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().delete(redisKey, hashKey);
    }

    public Map<Object, Object> getEntries(String redisKey){
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public Set<Object> getAllKeys(String redisKey) {
        return redisTemplate.opsForHash().keys(redisKey);
    }

}
