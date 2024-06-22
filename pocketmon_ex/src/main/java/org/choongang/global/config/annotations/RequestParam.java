package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
ElementType.PARAMETER는 메서드의 매개변수에 애노테이션을 적용할 수 있음을 의미합니다.
ElementType.TYPE_PARAMETER는 제네릭 타입 매개변수에 애노테이션을 적용할 수 있음을 의미함
@Retention(RetentionPolicy.RUNTIME) - 이 애노테이션의 유지 시점 정함, 런타임동안 유지
 */

/**
 * 이 애노테이션은 요청 파라미터를 메서드의 매개변수에 매핑한다 예를들어 URL에 포함된 쿼리 파라미터를 처리할때 사용함
 */

@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value();
}

/*
@GetMapping("/example")
public String exampleMethod(@RequestParam("name") String name, @RequestParam("age") int age) {
    // name과 age 파라미터를 이용한 로직
}

위의 경우  /example?name=John&age=30라는 URL 요청이 들어오면, name과 age의 값이 각각 John과 30으로 매핑되어 메서드의 파라미터로 전달된다.
 */