package org.choongang.member.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.member.services.JoinService;
import org.choongang.member.services.LoginService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;
    private final LoginService loginService;

    // 회원 가입 양식
    @GetMapping("/join")
    public String join() {

        return "member/join";
    }

    /*
    redirectURL 요청 있으면 로그인 이후에 그 페이지로 이동
    요청 없으면 메인 페이지로 이동
    - 사용자가 주문을 하다가 그 페이지에서 로그인을 하게 되면 다른 창으로 가면 또 들어와야하는 번거로움이 있음 그래서 로그인 전 들어갔던 페이지 띄우는게 좋음
     */

    // 회원 가입 처리
    @PostMapping("/join")
    public String joinPs(RequestJoin form, HttpServletRequest request) {

        joinService.process(form);

        String url = request.getContextPath() + "/member/login";
        String script = String.format("parent.location.replace('%s');", url);

        request.setAttribute("script", script);

        return "commons/execute_script";
    }

    // 로그인 양식
    @GetMapping("/login")
    public String login() {

        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginPs(RequestLogin form) {
        loginService.process(form);
        return "commons/execute_script";
    }
}