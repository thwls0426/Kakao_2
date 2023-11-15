package com.example.jebal.demo.kakao;

import com.example.jebal.demo.core.error.Exception.Exception401;
import com.example.jebal.demo.core.security.JwtTokenProvider;
import com.example.jebal.demo.user.User;
import com.example.jebal.demo.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;


@Slf4j
@Service
public class KakaoService {
    private final WebClient webClient;
    private final KakaoUri kakaoUri;
    private final UserRepository userRepository;
    private final KakaoDTO kakaoDTO;

    public KakaoService(WebClient.Builder webClientBuilder, KakaoUri kakaoUri, UserRepository userRepository, KakaoDTO kakaoDTO) {
        this.webClient = webClientBuilder.baseUrl("https://kauth.kakao.com").build();
        this.kakaoUri = kakaoUri;
        this.userRepository = userRepository;
        this.kakaoDTO = kakaoDTO;
    }
    public static void alert(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+msg+"');" +
                "location.href='/';</script>");
        out.flush();
    }

    @Transactional
    public String login(HttpServletResponse response) {
        try { // 카카오 로그인 정보를 User 객체로 변환하고 저장합니다.
            User user = userRepository.findByEmailAndProvider(kakaoDTO.getEmail(), kakaoDTO.getProvider())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .email(kakaoDTO.getEmail())
                            .username(kakaoDTO.getNickname())
                            .provider("kakao")
                            .roles(Collections.singletonList("ROLE_USER"))
                            .build()));

            alert(response, "회원가입이완료되었습니다");
            return JwtTokenProvider.create(user);


        } catch (Exception e) {
            // 401 반환.
            throw new Exception401("인증되지 않음.");
        }
    }

        public KakaoToken requestToken(String code) {
            try {
                String requestUrl = "/oauth/token"; //request를 보낼 주소


                //body요청을 위해 MultiValueMap 객체생성
                MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
                //파라미터 추가
                requestBody.add("grant_type", "authorization_code");
                requestBody.add("client_id", kakaoUri.getAPI_KEY());
                requestBody.add("redirect_uri", kakaoUri.getREDIRECT_URI());
                requestBody.add("code", code);

                KakaoToken kakaoToken = webClient.post()
                        .uri(requestUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(BodyInserters.fromFormData(requestBody))
                        .retrieve()
                        .bodyToMono(KakaoToken.class)
                        .block();

                if(kakaoToken != null){
                    String accessToken = kakaoToken.getAccess_token();
                    String refreshToken = kakaoToken.getRefresh_token();

                    kakaoToken.setAccess_token(accessToken);
                    kakaoToken.setRefresh_token(refreshToken);
                    kakaoToken.setCode(code);

                    log.info("access_token = {}", accessToken);
                    log.info("refresh_token = {}", refreshToken);

                    log.info("카카오토큰 생성 완료 >>> {}", kakaoToken);
                }
                return kakaoToken; //Step2 -> 토큰 발급 완료

            }catch (HttpClientErrorException ex){
                HttpStatus statusCode = ex.getStatusCode();
                if (statusCode != null) {
                    if (statusCode == HttpStatus.UNAUTHORIZED) {
                        // 인증 오류 처리
                        return null; // 또는 예외를 throw하여 클라이언트에게 전달
                    } else if (statusCode == HttpStatus.BAD_REQUEST) {
                        // 요청이 잘못된 경우 처리
                        return null; // 또는 예외를 throw하여 클라이언트에게 전달
                    }
                    // 다른 오류 상태 코드에 대한 처리
                }
                // 오류 응답에 대한 기타 처리
                return null; // 또는 예외를 throw하여 클라이언트에게 전달
            }
        }

        @SuppressWarnings("unchecked")
        public KakaoDTO requestUser(String accessToken){
            try {
                log.info("requestUser 시작");
                //발급받은 토큰으로 사용자 정보 조회 시작


                String userUrl = "https://kapi.kakao.com/v2/user/me";

                KakaoDTO user = webClient.get()
                        .uri(userUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(HashMap.class)
                        .map(resultMap -> {
                            // id 가져오기
                            Long id = Long.valueOf(String.valueOf(resultMap.get("id")));

                            // properties 가져오기
                            HashMap<String, Object> properties = (HashMap<String, Object>) resultMap.get("properties");
                            String nickname = (String) properties.get("nickname");
                            String thumbnail_image = (String) properties.get("thumbnail_image");

                            // kakao_account 가져오기
                            HashMap<String, Object> kakao_account = (HashMap<String, Object>) resultMap.get("kakao_account");
                            String email = null;
                            if (kakao_account != null && kakao_account.containsKey("email")) {
                                email = (String) kakao_account.get("email");
                            }

                            // KakaoResponse 객체 생성 및 값 설정
                            kakaoDTO.setEmail(email);
                            kakaoDTO.setId(id);
                            kakaoDTO.setNickname(nickname);
                            kakaoDTO.setThumbnail_image(thumbnail_image);

                            return kakaoDTO;
                        })
                        .block();
                return user;
            } catch (HttpClientErrorException ex) {
                // Kakao API에서 오류 응답을 받은 경우 처리
                HttpStatus statusCode = ex.getStatusCode();
                if (statusCode != null) {
                    if (statusCode == HttpStatus.UNAUTHORIZED) {
                        // 인증 오류 처리
                        return null; // 또는 예외를 throw하여 클라이언트에게 전달
                    } else if (statusCode == HttpStatus.BAD_REQUEST) {
                        // 요청이 잘못된 경우 처리
                        return null; // 또는 예외를 throw하여 클라이언트에게 전달
                    }
                    // 다른 오류 상태 코드에 대한 처리
                }
                // 오류 응답에 대한 기타 처리
                return null; // 또는 예외를 throw하여 클라이언트에게 전달
            }
        }
    }

