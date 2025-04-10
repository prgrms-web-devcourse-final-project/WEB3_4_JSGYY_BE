package com.ll.nbe344team7.global.security.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security.certified")
public class CertifiedProperties {
    public List<String> no;

    public List<String> getNo(){
        return no;
    }

    public void setNo(List<String> no){
        this.no = no;
    }
}
