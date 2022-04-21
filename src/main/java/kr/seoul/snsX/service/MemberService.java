package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.failedLogin;


public interface MemberService {
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto);

    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws failedLogin;

    public String searchLostMemberEmail(String nickName, String phoneNumber);

    public MemberInfoDto searchMember(String nickName);
}
