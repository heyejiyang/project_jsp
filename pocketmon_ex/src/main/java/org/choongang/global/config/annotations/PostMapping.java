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
 * POST 요청을 특정 URL 패턴과 매핑할 수 있다.. POST 요청은 주로 클라이언트가 서버에 데이터를 생성하거나 제출할 때 사용함
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {
    String[] value();
}