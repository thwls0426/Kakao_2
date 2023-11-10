package com.example.jebal.demo.kakao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


@Service
public class KakaoService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoService.class);
    private static final String CLIENT_ID = "d724c4c3a21fde9dd46123f7eb45872a";
    private static final String REDIRECT_URI = "http://localhost:8080/katalk/callback";

    private final KakaoRepository kakaoRepository;

    public KakaoService(KakaoRepository kakaoRepository) {
        this.kakaoRepository = kakaoRepository;
    }
    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=d724c4c3a21fde9dd46123f7eb45872a");
            sb.append("&redirect_uri=http://localhost:8080/katalk/callback");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;
    }

    public KakaoDTO getUserInfo(String access_Token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String result = callKakaoAPI("https://kapi.kakao.com/v2/user/me", access_Token);
        if (result != null) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
        }

        KakaoDTO kakaoDTO = kakaoRepository.findKakao(userInfo);
        if(kakaoDTO == null) {
            kakaoRepository.insertUser(userInfo);
            kakaoDTO = kakaoRepository.findKakao(userInfo);
        }
        return kakaoDTO;
    }

    public void Logout(String access_Token) {
        callKakaoAPI("https://kapi.kakao.com/v1/user/logout", access_Token);
    }

    public void unlink(String access_Token) {
        callKakaoAPI("https://kauth.kakao.com/oauth/logout", access_Token);
    }

    private String callKakaoAPI(String apiURL, String access_Token) {
        String result = null;
        try {
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            int responseCode = conn.getResponseCode();
            logger.info("responseCode : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            logger.info("response body : " + result);
        } catch (IOException e) {
            logger.error("Error during calling Kakao API", e);
        }
        return result;
    }
}


