package org.choongang.member.entities;

import lombok.Builder;
import lombok.Data;
import org.choongang.member.constants.UserType;

import java.time.LocalDateTime;

@Data
@Builder
public class Member {
    private long userNo;
    private String email;
    private String password;
    private String userName;
    private UserType userType = UserType.USER;//값을 설정하지 않을 경우 기본값은 일반 사용자
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
