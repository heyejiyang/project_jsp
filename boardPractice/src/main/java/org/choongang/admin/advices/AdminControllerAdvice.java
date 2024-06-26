package org.choongang.admin.advices;

import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.global.exceptions.UnAuthorizedException;
import org.choongang.member.MemberUtil;

@RequiredArgsConstructor
@ControllerAdvice("org.choongang.admin")
public class AdminControllerAdvice implements Interceptor {

    private final MemberUtil memberUtil;

    @Override
    public boolean preHandle() {
        if(!memberUtil.isAdmin()) {
            throw new UnAuthorizedException();
        }
        return true; //true이면 관리자 화면 보이고 false이면 안보임
        //관리자가 아니면 예외발생되도록할거임
    }
}
