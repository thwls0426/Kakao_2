package com.example.jebal.demo.kakao;

import org.springframework.web.bind.annotation.GetMapping;

public class successController {
    @GetMapping("/success")
    public String showLoginSuccessPage() {
        return "success"; // success.html 파일을 렌더링
    }

    @GetMapping("/logout-success")
    public String showLogoutSuccessPage() {
        return "logout-success"; // logout_success.html 파일을 렌더링
    }

    @GetMapping("/unlink-success")
    public String showUnlinkSuccessPage() {
        return "unlink-success"; // unlink_success.html 파일을 렌더링
    }
}
