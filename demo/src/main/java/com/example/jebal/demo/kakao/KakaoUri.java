package com.example.jebal.demo.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@Getter
public class KakaoUri {
    @Value("${kakao.api.key}")
    private String API_KEY; // REST API 키

    @Value("${kakao.redirect.uri}")
    private String REDIRECT_URI;
}
