package com.example.jebal.demo.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;

@Repository
public class KakaoRepository {

    @Autowired
    private SqlSessionTemplate sql;

    public void insertUser(HashMap<String, Object> userInfo) {
        sql.insert("KakaoMapper.insertUser",userInfo);
    }

    public HashMap<String, Object> findUserByEmail(String email) {
        return sql.selectOne("KakaoMapper.findUserByEmail", email);
    }

    // 정보 확인
    public KakaoDTO findKakao(HashMap<String, Object> userInfo) {
        System.out.println("RN:"+userInfo.get("nickname"));
        System.out.println("RE:"+userInfo.get("email"));
        HashMap<String, Object> foundUser = findUserByEmail((String) userInfo.get("email"));
        if(foundUser == null) {
            insertUser(userInfo);
            foundUser = findUserByEmail((String) userInfo.get("email"));
        }
        KakaoDTO kakaoDTO = new KakaoDTO();
        // DB에서 가져온 사용자 정보가 null이 아닌 경우에만 닉네임과 이메일을 설정
        if (foundUser != null) {
            kakaoDTO.setK_name((String) foundUser.get("nickname"));
            kakaoDTO.setK_email((String) foundUser.get("email"));
        }
        return kakaoDTO;
    }
}
