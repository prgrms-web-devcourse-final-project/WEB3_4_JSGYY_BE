package com.ll.nbe344team7.global.config.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 알람 redis sub 클래스
 *
 * @author 이광석
 * @since 25.04.08
 */
@Service
public class RedisNotificationSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisNotificationSubscriber.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisNotificationSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 알람을 웹소켓으로  받고 웹소켓으로 전달하는 메소드
     *
     * @param message
     * @param pattern
     *
     * @author 이광석
     * @since 25.04.08
     *
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            AlarmDTO alarmDTO = objectMapper.readValue((byte[]) message.getBody(),AlarmDTO.class);
            messagingTemplate.convertAndSend("/sub/notifications/"+alarmDTO.getReceiveMemberId());

        }catch (IOException e ){
            log.error("Redis Subscriber Error:",e);
        }
    }
}
