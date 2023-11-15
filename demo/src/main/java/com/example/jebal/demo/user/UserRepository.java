package com.example.jebal.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.example.jebal.demo.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // ** JPA는 메서드의 이름을 분석하는 방식으로 쿼리문을 작성할 수 있는 능력자
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, String provider);
}
