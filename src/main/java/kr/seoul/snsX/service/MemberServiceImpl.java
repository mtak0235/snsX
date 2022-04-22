package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupCacheDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.entity.Status;
import kr.seoul.snsX.entity.SignupCache;
import kr.seoul.snsX.exception.AlreadyExistException;
<<<<<<< HEAD
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.exception.FailedLoginException;
=======
import kr.seoul.snsX.exception.InputDataInvalidException;
import kr.seoul.snsX.exception.failedLogin;
>>>>>>> searchLostMemberEmail
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.util.List;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PostService postService;

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
    @Transactional
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto, String uuid) throws AlreadyExistException{
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
            return new MemberInfoDto(savedMember);
        } catch (IllegalArgumentException e) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        } finally {
            signupCache.expireEmail(memberSignupDto.getEmail());
            signupCache.expireNickName(memberSignupDto.getNickName());
        }
    }

    @Override
    @Transactional
    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws FailedLoginException {
        List<Member> memberByEmailAndPw = memberRepository.findMemberByEmailAndPw(memberLoginDto.getEmail(), memberLoginDto.getPw());
        if (memberByEmailAndPw.size() == 0)
            throw new FailedLoginException();
        return new MemberInfoDto(memberByEmailAndPw.get(0));
    }

    @Override
    @Transactional
    public String searchLostMemberEmail(String nickName, String phoneNumber) throws InputDataInvalidException {
        String email = memberRepository.findMemberEmailByNickNameAndPhoneNumber(nickName, phoneNumber);
        if (email == null)
            throw new InputDataInvalidException("잘못된 정보입니다");
        return email;
    }

    @Override
    public void searchLostMemberPw(String email) {
        Member member = memberRepository.findMemberByEmail(email);
        //추가 필요
    }

    @Override
    @Transactional
    public void removeMember(String password, Long memberId) throws FileNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 입니다."));
        if (!member.getPw().equals(password))
            throw new InvalidException("틀린 비밀번호 입니다.");
        member.setStatus(Status.INACTIVE);
        List<Post> posts = member.getPosts();
        for (Post post : posts) {
            postService.removePost(post.getId());
        }
    }
}
