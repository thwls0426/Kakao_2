package com.example.jebal.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.example.jebal.demo.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // ** JPA는 메서드의 이름을 분석하는 방식으로 쿼리문을 작성할 수 있는 능력자
    Optional<User> findByEmail(String email);

    public static String findUserByEmailOrPrintKakaoEmail(String kakaoEmail) {
        UserRepository userRepository = null;
        Optional<User> userOptional = userRepository.findByEmail(kakaoEmail);

        if (userOptional.isPresent()) {
            // 사용자가 존재하면 사용자의 이메일을 반환
            return userOptional.get().getEmail();
        } else {
            // 사용자가 존재하지 않으면 카카오에서 불러온 이메일을 반환
            return kakaoEmail;
        }
    }
}
