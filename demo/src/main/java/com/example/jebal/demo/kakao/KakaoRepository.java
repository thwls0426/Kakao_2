package com.example.jebal.demo.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;

@Repository
public class KakaoRepository {

    @Autowired
    private SqlSessionTemplate sql;

    public void kakaoInsert(HashMap<String, Object> userInfo) {
        sql.insert("Users.kakaoInsert",userInfo);
    }

    // 정보 확인
    public KakaoDTO findKakao(HashMap<String, Object> userInfo) {
        System.out.println("RN:"+userInfo.get("nickname"));
        System.out.println("RE:"+userInfo.get("email"));
        return sql.selectOne("Users.findKakao", userInfo);
    }

}
