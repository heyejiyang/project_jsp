package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
RestController라는 이름의 애노테이션을 정의

@Target(ElementType.TYPE) - 이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정, 이 애노테이션이 클래스/인터페이스/열거형에 적용될 수 있음을 의미함
@Retention(RetentionPolicy.RUNTIME) - 이 애노테이션의 유지 시점 정함, 런타임동안 유지
 */

/**
 * Rest 방식의 응답을 처리하는 컨트롤러, 반환값은 자바 객체, 문자열 등이 될 수 있으며 JSON 형식으로 변환하여 출력합니다.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {

}
