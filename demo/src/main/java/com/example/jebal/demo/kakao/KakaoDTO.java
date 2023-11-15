package com.example.jebal.demo.kakao;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
public class KakaoDTO {
    private Long id;
    private String email;
    private String nickname;
    private String thumbnail_image;
    private String properties;
    private String provider;

}

