package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Target({ElementType.TYPE, ElementType.METHOD})
 - 이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정
 ElementType.TYPE ->  애노테이션이 클래스, 인터페이스, 열거형 등에 적용될 수 있음을 의미
 ElementType.METHOD -> 메서드에 적용될 수 있음을 의미
@Retention(RetentionPolicy.RUNTIME) - 이 애노테이션의 유지 시점 정함, 런타임동안 유지
 */

/**
 * PATCH 요청을 특정 URL 패턴과 매핑할 수 있다. PATCH 요청은 주로 리소스의 일부를 업데이트할 때 사용한다.
 * PATCH 요청: 리소스의 일부만을 수정할때 사용 ( 리소스 -> 웹 애플리케이션의 특정 데이터 ex) 사용자, 블로그게시글, 제품정보 등)
 * ex) 클라이언트가 사용자 이메일만 변경하고싶을때, 부분 업데이트만 수행할때
 */


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatchMapping {
    String[] value();
}