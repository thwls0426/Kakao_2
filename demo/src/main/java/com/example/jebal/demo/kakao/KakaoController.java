package com.example.jebal.demo.kakao;


import com.example.jebal.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class KakaoController {

    private final KakaoService kakaoService;
    private final UserRepository userRepository;


    @GetMapping("/katalk/callback")
    public RedirectView kakaoLogin(@RequestParam String code, HttpSession session, HttpServletResponse response) throws IOException {

        System.out.println("code = " + code);
        //추가됨: 카카오 토큰 요청
        KakaoToken kakaoToken = kakaoService.requestToken(code);
        log.info("kakoToken = {}", kakaoToken);

    //추가됨: 유저정보 요청
        KakaoDTO kakaoDTO = kakaoService.requestUser(kakaoToken.getAccess_token());

        log.info("user = {}",kakaoDTO);

        session.setAttribute("access_token", kakaoToken.getAccess_token());

        log.info("토큰: " + String.valueOf(session.getAttribute("access_token")));

        kakaoService.login(response);

        return new RedirectView("/");
}
//    @RequestMapping(value = "/logout")
//    public String logout(HttpSession session) {
//        String access_Token = (String) session.getAttribute("access_Token");
//
//        if (access_Token != null && !"".equals(access_Token)) {
//            kakaoService.Logout(access_Token);
//            session.removeAttribute("access_Token");
//            session.removeAttribute("userId");
//        } else {
//            System.out.println("access_Token is null");
//        }
//        return "redirect:/logout_success.html";
//    }
//
//    @RequestMapping(value = "/unlink")
//    public String unlink(HttpSession session) {
//        String access_Token = (String) session.getAttribute("access_Token");
//
//        if (access_Token != null && !"".equals(access_Token)) {
//            kakaoService.unlink(access_Token);
//            session.invalidate();
//        } else {
//            System.out.println("access_Token is null");
//        }
//        return "redirect:/unlink_success.html";
//    }
}