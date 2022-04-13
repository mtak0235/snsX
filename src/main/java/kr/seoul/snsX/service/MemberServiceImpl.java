package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.UserInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public UserInfoDto registerUser(MemberSignupDto memberSignupDto) {

        Member savedMember = memberRepository.save(memberSignupDto.toEntity());
        return new UserInfoDto(savedMember);
    }

    @Override
    public String searchLostMemberEmail(String nickName, String phoneNumber) {
        return null;
    }
}
