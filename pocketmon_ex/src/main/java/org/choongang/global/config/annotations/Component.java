package org.choongang.global.config.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Component라는 이름의 애노테이션을 정의

@Target(ElementType.TYPE) //이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정, 이 애노테이션이 클래스/인터페이스/열거형에 적용될 수 있음을 의미함
@Retention(RetentionPolicy.RUNTIME)//이 애노테이션의 유지 시점 정함, 런타임동안 유지
 */

@Target(ElementType.TYPE) //이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정, 이 애노테이션이 클래스/인터페이스/열거형에 적용될 수 있음을 의미함
@Retention(RetentionPolicy.RUNTIME)//이 애노테이션의 유지 시점 정함, 런타임동안 유지
public @interface Component {
}
