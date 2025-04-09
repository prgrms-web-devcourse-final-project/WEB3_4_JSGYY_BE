package com.ll.nbe344team7.global.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("4차 프로젝트 7팀 API").version("v1"))
                .servers(List.of(
                        new Server().url("https://api.app1.springservice.shop").description("PROD"),
                        new Server().url("http://localhost:8080").description("DEV")

                ))
                .addSecurityItem(new SecurityRequirement()
                        .addList("access-token"))
                .components(new Components()
                        .addSecuritySchemes("access-token", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("access"))
                );
    }

    @Bean
    public GroupedOpenApi accountApi() {
        return GroupedOpenApi.builder()
                .group("AccountApi")
                .pathsToMatch("/api/account/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("AdminApi")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alarmApi() {
        return GroupedOpenApi.builder()
                .group("AlarmApi")
                .pathsToMatch("/api/alarms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi auctionApi() {
        return GroupedOpenApi.builder()
                .group("AuctionApi")
                .pathsToMatch("/api/auction/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chatApi() {
        return GroupedOpenApi.builder()
                .group("ChatApi")
                .pathsToMatch("/api/chat/rooms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi followApi() {
        return GroupedOpenApi.builder()
                .group("FollowApi")
                .pathsToMatch("/api/follow/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("MemberApi")
                .pathsToMatch("/api/member/**")
                .build();
    }

    @Bean
    public GroupedOpenApi payApi() {
        return GroupedOpenApi.builder()
                .group("PayApi")
                .pathsToMatch("/api/pay/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postApi() {
        return GroupedOpenApi.builder()
                .group("PostApi")
                .pathsToMatch("/api/posts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("AuthApi")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reissueApi() {
        return GroupedOpenApi.builder()
                .group("ReissueApi")
                .pathsToMatch("/api/reissue/**")
                .build();
    }

    @Bean GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("LoginApi")
                .pathsToMatch("/api/login")
                .build();
    }
}
