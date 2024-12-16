package sg.edu.nus.iss.mini_project.repository;

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

}
