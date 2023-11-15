package com.example.jebal.demo.user;

import com.example.jebal.demo.core.error.Exception.Exception400;
import com.example.jebal.demo.core.error.Exception.Exception401;
import com.example.jebal.demo.core.error.Exception.Exception500;
import com.example.jebal.demo.core.security.CustomUserDetails;
import com.example.jebal.demo.core.security.JwtTokenProvider;
import com.example.jebal.demo.core.utils.SignUpMessageSender;
import com.example.jebal.demo.kakao.KakaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        checkEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        requestDTO.setPassword(encodedPassword);

        try {
            userRepository.save(requestDTO.toEntity(passwordEncoder));

            SignUpMessageSender.sendMessage("01088224115", requestDTO.getPhoneNumber()
                    , "환영합니다. 회원가입이 완료되었습니다.");

        } catch (Exception e) {
            throw new Exception500(e.getMessage());
        }
    }

    public String login(UserRequest.JoinDTO requestDTO) {
        // ** 인증 작업.
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );

            // ** 인증 완료 값을 받아온다.
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // ** 토큰 발급.
            return JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception e) {
            // 401 반환.
            throw new Exception401("인증되지 않음.");
        }


    }
    public void findAll() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            user.output();
        }
    }

    public void checkEmail(String email) {
        // 동일한 이메일이 있는지 확인.
        Optional<User> users = userRepository.findByEmail(email);
        if (users.isPresent()) {
            throw new Exception400("이미 존재하는 이메일 입니다. : " + email);
        }
    }

    public UserRequest findByEmailAndProvider(String email, String provider){
        // 동일한 이메일이 있는지 확인.
        Optional<User> users = userRepository.findByEmailAndProvider(email, provider);
        if(users.isPresent()) {
            throw new Exception400("이미 존재하는 이메일 입니다. : " + email);
        }
        return null;
    }

}
//    public static OAuth2UserService<OAuth2UserRequest, OAuth2User> KakaoOAuth2UserService;

//    public void createUserFromKakao(KakaoDTO kakaoUser) {
//        User user = new User();
//        user.setEmail(kakaoUser.getEmail());
//        user.setNickname(kakaoUser.getNickname());
//        // ... 다른 필드들도 적절히 설정 ...
//
//        userRepository.save(user);
//    }
