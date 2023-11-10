package com.example.jebal.demo.kakao;

import com.example.jebal.demo.user.User;
import com.example.jebal.demo.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class KakaoRepository {

    private final UserRepository userRepository;

    public KakaoRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insertUser(HashMap<String, Object> userInfo) {
        User user = new User();
        user.setEmail((String) userInfo.get("email"));
        user.setNickname((String) userInfo.get("nickname"));
        user.setPassword((String) userInfo.get("password"));
        user.setUsername((String) userInfo.get("username"));
        user.setPhoneNumber((String) userInfo.get("phoneNumber"));
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다. 이메일: " + email));
    }

    // 정보 확인
    public KakaoDTO findKakao(HashMap<String, Object> userInfo) {

        String email = (String) userInfo.get("email");
        String nickname = (String) userInfo.get("nickname");

        if (email == null || nickname == null) {
            throw new IllegalArgumentException("이메일과 닉네임은 null일 수 없습니다.");
        }

        User foundUser = findUserByEmail(email);
        if (foundUser == null) {
            insertUser(userInfo);
            foundUser = findUserByEmail(email);
        }

        KakaoDTO kakaoDTO = new KakaoDTO();
        kakaoDTO.setK_nickname(foundUser.getNickname());
        kakaoDTO.setK_email(foundUser.getEmail());

        return kakaoDTO;
    }
}
