package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.entity.Status;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.exception.FailedLoginException;
import kr.seoul.snsX.repository.FileRepository;
import kr.seoul.snsX.repository.MemberRepository;
import kr.seoul.snsX.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;

    @Override
    @Transactional
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto) throws AlreadyExistException{
        if (memberRepository.existsMemberByEmail(memberSignupDto.getEmail())) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        }
        Member savedMember = memberRepository.save(memberSignupDto.toEntity());
        return new MemberInfoDto(savedMember);
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
    public String searchLostMemberEmail(String nickName, String phoneNumber) {
        return null;
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
