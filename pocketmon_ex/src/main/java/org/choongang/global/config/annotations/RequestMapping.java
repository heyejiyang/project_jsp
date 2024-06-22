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
 * 이 애노테이션은 클래스나 메서드에 적용하여 특정 URL패턴과 요청을 매핑하는데 사용
 * 클래스 상단에 사용하면 클래스 내 모든 메서드에 공통된 URL 접두어를 지정할 수 있으며, 메서드 상단에 사용하면 해당 메서드에 대한 구체적인 URL 패턴을 지정할 수 있다.
 * -> 클래스 레벨의 @RequestMapping애노테이션과 메서드 레벨의 매핑 애노테이션이 결합되어 최종 경로가 형성될 수 있다.
 *
 *  HTTP 메서드(GET, POST, PUT, DELETE 등)와 상관없이 모든 요청을 특정 URL 패턴에 매핑할 수 있는 범용 애노테이션
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String[] value(); // 요청 URL 패턴
}

/*예시
@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/join")
    public String join(RequestJoin form) {
    }
    @GetMapping("/profile")
    public String profile() {
    }
    @PostMapping("/update")
    public String update(RequestJoin form) {
    }
}
 */