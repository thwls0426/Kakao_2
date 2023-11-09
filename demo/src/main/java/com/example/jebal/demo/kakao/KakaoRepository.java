package com.example.jebal.demo.kakao;

import com.example.jebal.demo.user.User;
import com.example.jebal.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    // 정보 확인
    public KakaoDTO findKakao(HashMap<String, Object> userInfo) {
        System.out.println("RN:"+userInfo.get("nickname"));
        System.out.println("RE:"+userInfo.get("email"));
        User foundUser = findUserByEmail((String) userInfo.get("email"));
        if(foundUser == null) {
            insertUser(userInfo);
            foundUser = findUserByEmail((String) userInfo.get("email"));
        }
        KakaoDTO kakaoDTO = new KakaoDTO();
        // DB에서 가져온 사용자 정보가 null이 아닌 경우에만 닉네임과 이메일을 설정
        if (foundUser != null) {
            kakaoDTO.setK_name(foundUser.getNickname());
            kakaoDTO.setK_email(foundUser.getEmail());
        }
        return kakaoDTO;
    }
}
