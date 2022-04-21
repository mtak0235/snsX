package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.InputDataInvalidException;
import kr.seoul.snsX.exception.failedLogin;
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

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
    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws failedLogin {
        Member member = memberRepository.findMemberByEmailAndPw(memberLoginDto.getEmail(), memberLoginDto.getPw());
        if (member == null)
            throw new failedLogin();
        return new MemberInfoDto(member);
    }

    @Override
    public String searchLostMemberEmail(String nickName, String phoneNumber) throws InputDataInvalidException {
        String email = memberRepository.findMemberEmailByNickNameAndPhoneNumber(nickName, phoneNumber);
        if (email == null)
            throw new InputDataInvalidException("잘못된 정보입니다");
        return email;
    }

    @Override
    public void searchLostMemberPw(String email) {
        Member member = memberRepository.findMemberByEmail(email);
        
    }
}
