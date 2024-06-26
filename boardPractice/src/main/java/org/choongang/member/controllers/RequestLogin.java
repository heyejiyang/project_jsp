package org.choongang.member.controllers;

import lombok.Data;

@Data
public class RequestLogin {
    private String email;
    private String password;
    private String saveEmail;
    private String redirectUrl;
}
/*
    redirectURL 요청 있으면 로그인 이후에 그 페이지로 이동
    요청 없으면 메인 페이지로 이동
    - 사용자가 주문을 하다가 그 페이지에서 로그인을 하게 되면 다른 창으로 가면 또 들어와야하는 번거로움이 있음 그래서 로그인 전 들어갔던 페이지 띄우는게 좋음
*/