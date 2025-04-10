package com.ll.nbe344team7.global.cors;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 설정
 *
 * @since 2025-03-26
 * @author 이광석
 */
public class CorsMvcConfig implements WebMvcConfigurer {

    /**
     * CORS 설정 메소드
     * @param corsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry
                .addMapping("/**")   //모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000");  //3000에서 오는 요청만 허용
    }
}
