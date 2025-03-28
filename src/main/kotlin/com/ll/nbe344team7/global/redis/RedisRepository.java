package com.ll.nbe344team7.global.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {
    private final RedisTemplate<String,String> redisTemplate;

    public RedisRepository(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void save(String key,String value, Long limit){
        redisTemplate.opsForValue().set(key,value,limit, TimeUnit.MICROSECONDS);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
