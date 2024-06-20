package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Controller라는 이름의 애노테이션을 정의

@Target(ElementType.TYPE) - 이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정, 이 애노테이션이 클래스/인터페이스/열거형에 적용될 수 있음을 의미함
@Retention(RetentionPolicy.RUNTIME) - 이 애노테이션의 유지 시점 정함, 런타임동안 유지
 */

/**
 * 일반적인 요청과 응답을 처리하는 컨트롤러 각 요청 메서드의 반환값은 String이며 이 문자열은 템플릿 경로가 됩니다. 예를 들어 반환값이 member/join 이라면 webapp/WEB-INF/templates/member/join.jsp를 찾고 해당 뷰를 RequestDispatcher를 통해서 버퍼에 추가하고 출력 합니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}
