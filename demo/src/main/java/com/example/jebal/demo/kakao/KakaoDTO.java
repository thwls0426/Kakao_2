package com.example.jebal.demo.kakao;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class KakaoDTO {
    private long k_number;
    private String k_name;
    private String k_email;

}

