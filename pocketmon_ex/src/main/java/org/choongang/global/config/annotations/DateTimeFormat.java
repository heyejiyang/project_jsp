package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 요청 파라미터로 받은 문자열을 특정 형식으로 날짜/시간 객체로 변환할 수 있다.
 * pattern 속성을 통해 변환할 문자열의 형식을 지정한다.
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormat {
    String value();
}

/*
@Data
public class RequestJoin {
    // 다른 멤버 변수들

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt;
}
 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 애노테이션은 regDt 필드에 적용되어, yyyy-MM-dd HH:mm:ss 형식의 문자열을 LocalDateTime 객체로 변환한다.
 */
/*
 @Controller
@RequestMapping("/member")
public class MemberController {
    private final JoinService joinService;

    @GetMapping("/join")
    public String join(RequestJoin form) {

        ...

        return "member/join";
    }
}
/member/join 경로로 요청을 보낼 때, 요청 파라미터에 regDt가 포함되어 있다면, 해당 파라미터의 문자열 값은 RequestJoin 클래스의 regDt 필드에 매핑되며 LocalDateTime 객체로 변환된다.
 */