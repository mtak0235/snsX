package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.FailedLoginException;

import java.io.FileNotFoundException;

public interface MemberService {
    public String occupyEmail(String email, String uuid);

    public String occupyNickName(String nickName, String uuid);

    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto, String uuid);

    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws FailedLoginException;

    public String searchLostMemberEmail(String nickName, String phoneNumber);

    public void removeMember(String password, Long memberId) throws FileNotFoundException;
}
