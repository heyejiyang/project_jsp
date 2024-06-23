package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Servlet 기반의 웹 애플리케이션에서 특정 요청을 처리하기 위한 어댑터 역할을 수행하는 메서드를 정의함
 *
 * 서로 다른 컨트롤러들이 각자의 방식으로 요청을 처리하고자 할 때, 각각의 컨트롤러가 HandlerAdapter 인터페이스를 구현하여 일관된 방식으로 요청을 처리할 수 있다.
 */
public interface HandlerAdapter {
    void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data);
    //요청을 처리하고 그에 따른 응답을 생성하는 역할
}