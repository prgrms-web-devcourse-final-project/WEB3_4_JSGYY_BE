package com.ll.nbe344team7.global.config.redis.publisher;


import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.member.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 알람 redis pub
 *
 * @author 이광석
 * @since 25.04.08
 */
@Component
public class AlarmRedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public AlarmRedisPublisher(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void publishMessage(AlarmDTO alarmDTO) {
        redisTemplate.convertAndSend("notification", alarmDTO);
    }

}
