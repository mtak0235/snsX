package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.UserInfoDto;


public interface MemberService {
    public UserInfoDto registerUser(MemberSignupDto memberSignupDto);

}
