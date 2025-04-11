package com.ll.nbe344team7.global.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DB에서 가져온 사용자 정보를 담는 객체
 * 로그인시 : AuthenticationManager 가 CustomUserDetails를 이용하여 입력받은 로그인 정보와 대조
 * 로그인 외 다른 요청시 : JWT토큰에서 사용자 정보를 추출하여 저장
 *
 * @author 이광석
 * @since 2025-03-26
 */
public class CustomUserDetails implements UserDetails {

    final private CustomUserData customUserData;

    public CustomUserDetails(
            CustomUserData customUserData) {
        this.customUserData = customUserData;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customUserData.getRole();
            }
        });
        return collection;
    }

    public Long getMemberId() {
        return customUserData.getMemberId();
    }

    public String getRole() {
        return customUserData.getRole();
    }

    @Override
    public String getPassword() {
        return customUserData.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserData.getUsername();
    }

    public String getNickname() {
        return customUserData.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
