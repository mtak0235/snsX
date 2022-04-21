package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupCacheDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.entity.SignupCache;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.failedLogin;
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    @Autowired
    private final SignupCache signupCache = new SignupCache();

    @Override
    public String occupyEmail(String email, String uuid) throws AlreadyExistException {
        MemberSignupCacheDto memberSignupCacheDto = signupCache.isUsableEmail(email, uuid);
        if (memberSignupCacheDto.isFlag()) {
            if (memberSignupCacheDto.getUuid() == null) {
                throw new AlreadyExistException("이미 존재하는 email입니다");
            } else {
                return memberSignupCacheDto.getUuid();
            }
        }
        if (!memberRepository.existsMemberByEmail(email)) {
            throw new AlreadyExistException("이미 존재하는 email입니다");
        }
        return signupCache.createEmailCache(email);
    }

    @Override
    public String occupyNickName(String nickName, String uuid) throws AlreadyExistException {
        MemberSignupCacheDto memberSignupCacheDto = signupCache.isUsableEmail(nickName, uuid);
        if (memberSignupCacheDto.isFlag()) {
            if (memberSignupCacheDto.getUuid() == null) {
                throw new AlreadyExistException("이미 존재하는 nickName입니다");
            } else {
                return memberSignupCacheDto.getUuid();
            }
        }
        if (!memberRepository.existsMemberByEmail(nickName)) {
            throw new AlreadyExistException("이미 존재하는 nickName입니다");
        }
        return signupCache.createEmailCache(nickName);
    }

    @Override
    public void registerMember(MemberSignupDto memberSignupDto, String uuid) throws AlreadyExistException{
        MemberSignupCacheDto memberSignupCacheDto = signupCache.isUsableEmail(memberSignupDto.getEmail(), uuid);
        if (memberSignupCacheDto.getUuid() == null) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        }
        memberSignupCacheDto = signupCache.isUsableNickName(memberSignupDto.getNickName(), memberSignupCacheDto.getUuid());
        if (memberSignupCacheDto.getUuid() == null) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        }
        try {
            Member savedMember = memberRepository.save(memberSignupDto.toEntity());
        } catch (IllegalArgumentException e) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        } finally {
            signupCache.expireEmail(memberSignupDto.getEmail());
            signupCache.expireNickName(memberSignupDto.getNickName());
        }
    }

    @Override
    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws failedLogin {
        Member member = memberRepository.findMemberByEmailAndPw(memberLoginDto.getEmail(), memberLoginDto.getPw());
        if (member == null)
            throw new failedLogin();
        return new MemberInfoDto(member);
    }

    @Override
    public String searchLostMemberEmail(String nickName, String phoneNumber) {
        return null;
    }
}
