package com.example.jebal.demo.kakao;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KakaoToken {
    String token_type;
    String access_token;
    Integer expires_in;
    String refresh_token;
    Integer refresh_token_expires_in;
    String scope;
    String code;
}
