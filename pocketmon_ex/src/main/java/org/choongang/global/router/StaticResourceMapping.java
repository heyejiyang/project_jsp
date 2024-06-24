package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface StaticResourceMapping {
    // 정적 경로인지 체크
    boolean check(HttpServletRequest request); //현재 요청된 URL이 동적 요청이 아닌, 정적 경로에 해당하는지 확인
    //webapp/static 디렉토리에 요청된 자원이 존재하는지 확인

    void route(HttpServletRequest request, HttpServletResponse response) throws IOException; //정적 자원이 발견되면, 해당 파일의 내용을 클라이언트에게 반환
    //정적 자원의 파일 형식(예: CSS 파일, 이미지 파일 등)을 확인하여, 해당 형식에 맞는 content-type 헤더를 응답에 추가
}