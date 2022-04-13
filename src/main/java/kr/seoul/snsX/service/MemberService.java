package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;


public interface MemberService {
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto);

    public String searchLostMemberEmail(String nickName, String phoneNumber);
}
