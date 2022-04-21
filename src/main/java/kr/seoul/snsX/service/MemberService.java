package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.failedLogin;


public interface MemberService {
    public String occupyEmail(String email, String uuid);

    public String occupyNickName(String nickName, String uuid);

    public void registerMember(MemberSignupDto memberSignupDto, String uuid);

    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws failedLogin;

    public String searchLostMemberEmail(String nickName, String phoneNumber);
}
