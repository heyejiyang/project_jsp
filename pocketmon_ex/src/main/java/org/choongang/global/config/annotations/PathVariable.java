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
 * 이 애노테이션은 메서드 매개변수에 사용되어 경로 변수 값을 매핑하는 데 사용됩니다. (URL경로의 일부를 메서드 매개변수에 매핑하기 위해 사용)
 */

@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {
    String value();
}


/*
@GetMapping("/users/{userId}")
public String getUser(@PathVariable("userId") String userId) {
    // userId를 이용한 로직
}
위의 경우, /users/123라는 URL 요청이 들어오면, userId의 값이 123으로 매핑되어 메서드의 파라미터로 전달
 */

/*
@GetMapping("/{mode}")
public String join(@PathVariable("mode") String mode, @RequestParam("seq") int seq) {
    // mode와 seq를 이용한 로직
}
위의 경우, /someMode?seq=10이라는 요청이 들어오면, mode는 경로 변수로서 someMode로 매핑되고, seq는 요청 파라미터로서 10으로 매핑
 */