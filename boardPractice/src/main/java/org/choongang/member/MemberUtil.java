package org.choongang.member;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.constants.UserType;
import org.choongang.member.entities.Member;

@Component
@RequiredArgsConstructor
public class MemberUtil {
    //로그인 여부
    private final HttpSession session;
    public boolean isLogin(){
        return getMember() != null;//null이 아닐때는 로그인 상태
    }

    //관리자 여부
    public boolean isAdmin(){
        if(isLogin()){
            Member member = getMember();
            return member.getUserType() == UserType.ADMIN; //usertype이 Admin인 경우 관리자 권한부여
        }
        return false;
    }
    /*
    로그인한 회원 정보
    @return
     */
    public Member getMember() {
        HttpSession session = BeanContainer.getInstance().getBean(HttpSession.class);
        Member member = (Member)session.getAttribute("member");
        return member;
    }
}
