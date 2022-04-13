package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto) throws AlreadyExistException{
        if (memberRepository.existsMemberByEmail(memberSignupDto.getEmail())) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        }
        Member savedMember = memberRepository.save(memberSignupDto.toEntity());
        return new MemberInfoDto(savedMember);
    }

    @Override
    public String searchLostMemberEmail(String nickName, String phoneNumber) {
        return null;
    }
}
