package com.example.jebal.demo.kakao;

import org.springframework.web.bind.annotation.GetMapping;

public class successController {
    @GetMapping("/login")
    public String showLoginSuccessPage() {
        return "login"; // login.html 파일을 렌더링
    }

    @GetMapping("/logout_success")
    public String showLogoutSuccessPage() {
        return "logout_success"; // logout_success.html 파일을 렌더링
    }

    @GetMapping("/unlink_success")
    public String showUnlinkSuccessPage() {
        return "unlink_success"; // unlink_success.html 파일을 렌더링
    }
}
