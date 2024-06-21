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
 * HTTP PUT 요청을 특정 URL 패턴에 매핑하기 위해 사용됩니다. PUT 요청은 주로 리소스의 전체를 업데이트할 때 사용
 * ex)
 * PUT /posts/1: ID가 1인 블로그 게시글 전체 내용 업데이트.
 * PATCH /posts/1: ID가 1인 블로그 게시글의 일부 내용 업데이트.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PutMapping {
    String[] value();
}