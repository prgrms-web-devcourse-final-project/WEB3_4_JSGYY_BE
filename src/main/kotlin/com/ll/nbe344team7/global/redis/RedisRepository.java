package com.ll.nbe344team7.global.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Redis repository
 * redis 관련 기능들구현
 */
@Repository
public class RedisRepository {
    private final RedisTemplate<String,String> redisTemplate;


    public RedisRepository(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * redis 에 저장
     * @param key
     * @param value
     * @param limit
     * @author 이광석
     * @since 2025-03-28
     */
    public void save(String key,String value, Long limit){
        redisTemplate.opsForValue().set(key,value,limit, TimeUnit.MILLISECONDS);
    }

    /**
     * redis 조회 및 반환
     * @param key
     * @return
     * @author 이광석
     * @since 2025-03-28
     */
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * redis에서 데이터 삭제
     * @param key
     * @author 이광석
     * @since 2025-03-28
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     * value 수정
     * @param key
     * @param newValue
     * @author 이광석
     * @since 2025-03-28
     */
    public void modify(String key, String newValue){
        Long limit = redisTemplate.getExpire(key,TimeUnit.MILLISECONDS);
        delete(key);
        save(key,newValue,limit);
    }

}
