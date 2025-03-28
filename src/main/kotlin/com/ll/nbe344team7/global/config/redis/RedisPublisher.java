package com.ll.nbe344team7.global.config.redis;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private static final Logger log = LoggerFactory.getLogger(RedisPublisher.class);
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public RedisPublisher(ChannelTopic channelTopic, RedisTemplate redisTemplate) {
        this.channelTopic = channelTopic;
        this.redisTemplate = redisTemplate;
    }

    public void publish(MessageDTO message) {
        log.info("Publishing message: {}", message);
        redisTemplate.convertAndSend(channelTopic.getTopic()+message.getRoomId(), message);
    }
}
