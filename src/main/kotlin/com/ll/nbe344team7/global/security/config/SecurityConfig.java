package com.ll.nbe344team7.global.security.config;


import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.jwt.JWTFilter;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import com.ll.nbe344team7.global.security.jwt.LoginFilter;
import com.ll.nbe344team7.global.security.jwt.LogoutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * 시큐리티 설정
 *
 * @since 2025-03-25
 * @author  이광석
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;

    public SecurityConfig(
            AuthenticationConfiguration authenticationConfiguration,
            JWTUtil jwtUtil,
            RedisRepository redisRepository) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }


    @Value("${security.certified.no}")
    private List<String> noCertifiedUrl;

    /**
     * AuthenticationManager 를 bean 으로 등록하는 메서드
     * AuthenticationManager 는 입력받은 로그인정보와 UserDetailsService 등을 통해 DB의 정보를 비교하여 로그인 가능한지 확인함
     * @param configuration
     * @return AuthenticationManager
     * @throws Exception
     * @author 이광석
     * @since 2025-03-25
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * 인코더를 bean으로 등록하는 메서드
     * 해당 인코더는 password를 인코딩하는데 사용
     * @return BCryptPasswordEncoder
     * @author 이광석
     * @since 2025-03-25
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://lustrous-pegasus-1dc714.netlify.app"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }


    /**
     * SecurityFilterChain을 bean 으로 등록하는 메서드
     * HTTP 요청시 보안처리 필터 체인
     * @param http
     * @return SecurityFilterChain
     * @throws Exception
     * @author 이광석
     * @since 2025-03-25
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil, redisRepository);
        loginFilter.setFilterProcessesUrl("/api/auth/login");




        http
                .csrf((auth) -> auth.disable())

                .formLogin((auth) -> auth.disable())

                .httpBasic((auth) -> auth.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests((auth) -> auth

                        .requestMatchers("/api/login","/api/reissue", "/login", "/", "/api/auth/register","/h2-console/**", "/ws/**","/swagger-ui/**","/v3/api-docs/**", "/actuator/health").permitAll()  //인증없이 접속가능
                        .anyRequest().authenticated()) // 인증 필요

                .headers(headers -> headers
                        .defaultsDisabled()
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .addFilterBefore(new JWTFilter(jwtUtil,redisRepository,noCertifiedUrl),UsernamePasswordAuthenticationFilter.class)  // jwt 유효성 검사

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 유효성 검사

                .addFilterBefore(new LogoutFilter(jwtUtil, redisRepository), LoginFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //세션 stateless 상태 설정

        return http.build();
    }
}