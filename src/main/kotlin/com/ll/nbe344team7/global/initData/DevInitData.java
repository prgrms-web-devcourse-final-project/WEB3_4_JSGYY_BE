package com.ll.nbe344team7.global.initData;

import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

/**
 * @author shjung
 * @since 25. 4. 11.
 */
@Profile("dev")
@Configuration
public class DevInitData {
    private final MemberService memberService;
    private final PostService postService;
    @Autowired
    @Lazy
    private DevInitData self;

    public DevInitData(MemberService memberService, PostService postService) {
        this.memberService = memberService;
        this.postService = postService;
    }

    @Bean
    public ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            Ut.file.downloadByHttp("http://localhost:8080/v3/api-docs", ".");

            String cmd = "yes | npx --package typescript --package openapi-typescript openapi-typescript api-docs.json -o ../testfolder/my-app/src/lib/backend/apiV1/schema.d.ts";
            Ut.cmd.runAsync(cmd);
        };
    }
}
