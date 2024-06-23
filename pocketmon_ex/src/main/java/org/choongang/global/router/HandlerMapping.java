package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 웹 애플리케이션의 요청을 처리할 때, 특정 URL 패턴에 맞는 컨트롤러와 그 메서드를 찾아주는 역할을 한다. 들어오는 HTTP요청을 기반으로 어떤 컨트롤러와 그 안의 어떤 메서드를 호출할지 결정함
 */
public interface HandlerMapping {
    List<Object> search(HttpServletRequest request);

}