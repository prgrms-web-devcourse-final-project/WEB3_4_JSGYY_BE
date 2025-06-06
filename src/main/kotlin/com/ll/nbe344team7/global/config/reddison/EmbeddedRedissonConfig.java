//package com.ll.nbe344team7.global.config.reddison;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//@Configuration
//@DependsOn("embeddedRedisConfig")
//public class EmbeddedRedissonConfig {
//    @Bean
//    public RedissonClient embeddedRedissonClient() {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://127.0.0.1:6379")
//                .setConnectionPoolSize(10)
//                .setConnectionMinimumIdleSize(5);
//        return Redisson.create(config);
//    }
//}
