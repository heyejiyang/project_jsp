package org.choongang.member.mapper;

import org.choongang.member.entities.Member;

public interface MemberMapper {
    Member get(String email); //메서드 명의 기준은 mapper.xml의 id값임
    int exists(String email);
    int register(Member member);
}
