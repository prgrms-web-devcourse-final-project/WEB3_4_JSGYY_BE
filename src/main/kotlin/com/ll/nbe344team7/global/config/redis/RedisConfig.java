package com.ll.nbe344team7.global.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ll.nbe344team7.global.config.redis.subscriber.RedisChatRoomListSubscriber;
import com.ll.nbe344team7.global.config.redis.subscriber.RedisChatSubscriber;
import com.ll.nbe344team7.global.config.redis.subscriber.RedisNotificationSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean(name = "redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.deactivateDefaultTyping();
        return objectMapper;
    }

    @Bean(name = "chatMessageListener")
    public MessageListenerAdapter chatMessageListener(RedisChatSubscriber chatSubscriber) {
        return new MessageListenerAdapter(chatSubscriber, "onMessage");
    }

    @Bean(name = "notificationListener")
    public MessageListenerAdapter notificationListener(RedisNotificationSubscriber notificationSubscriber) {
        return new MessageListenerAdapter(notificationSubscriber, "onMessage");
    }

    @Bean(name = "ChatRoomListListener")
    public MessageListenerAdapter ChatRoomListListener(RedisChatRoomListSubscriber chatRoomListSubscriber) {
        return new MessageListenerAdapter(chatRoomListSubscriber, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            @Qualifier("chatMessageListener") MessageListenerAdapter chatListener,
            @Qualifier("notificationListener") MessageListenerAdapter notificationListener,
            @Qualifier("chatChannel") ChannelTopic chatChannel,
            @Qualifier("notificationChannel") ChannelTopic notificationChannel,
            @Qualifier("ChatRoomListListener") MessageListenerAdapter ChatRoomListListener,
            @Qualifier("chatRoomListChannel") ChannelTopic ChatRoomListChannel
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatListener, chatChannel); // ChannelTopic 'chatroom' 사용
        container.addMessageListener(notificationListener, notificationChannel); // ChannelTopic 'notification' 사용
        container.addMessageListener(ChatRoomListListener, ChatRoomListChannel);
        return container;
    }

    // 메세지 전송 채널
    @Bean(name = "chatChannel")
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }

    // 알림용 채널
    @Bean(name = "notificationChannel")
    public ChannelTopic notificationChannel() {
        return new ChannelTopic("notification");
    }

    @Bean(name = "chatRoomListChannel")
    public ChannelTopic ChatRoomListChannel() {
        return new ChannelTopic("chatroomList"); // 'chatroomList' 사용
    }
}