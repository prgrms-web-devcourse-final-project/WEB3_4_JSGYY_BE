//package com.ll.nbe344team7.global.config.web;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurer() {
//             @Override
//            public void addCorsMappings(CorsRegistry registry){
//             registry.addMapping("/**")
//                     .allowedOrigins("http://localhost:3000")
//                     .allowedOrigins("https://www.app1.springservice.shop")
//                     .allowedMethods("GET","POST","DELETE","PATCH","OPTIONS")
//                     .allowedHeaders("*")
//                     .allowCredentials(true);
//             }
//        };
//    }
//}
