package org.choongang.member.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.*;
import org.choongang.member.services.JoinService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/{mode}/test/{num}")
    public String join(@PathVariable("mode") String mode, @RequestParam("seq") int seq, RequestJoin form, HttpServletRequest request, HttpServletResponse response, @PathVariable("num") int num) {

        System.out.printf("mode=%s, seq=%d, num=%d%n", mode, seq, num);
        System.out.println(form);

        return "member/join";
    }
}