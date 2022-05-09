package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.entity.*;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.exception.FailedLoginException;
import kr.seoul.snsX.exception.InputDataInvalidException;
import kr.seoul.snsX.repository.FileRepository;
import kr.seoul.snsX.repository.FollowRepository;
import kr.seoul.snsX.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.util.List;

import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final FollowRepository followRepository;

    private final FileRepository fileRepository;
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
        if (memberRepository.existsMemberByEmail(email)) {
            throw new AlreadyExistException("이미 존재하는 email입니다");
        }
        return signupCache.createEmailCache(email, uuid);
    }

    @Override
    public String occupyNickName(String nickName, String uuid) throws AlreadyExistException {
        MemberSignupCacheDto memberSignupCacheDto = signupCache.isUsableNickName(nickName, uuid);
        if (memberSignupCacheDto.isFlag()) {
            if (memberSignupCacheDto.getUuid() == null) {
                throw new AlreadyExistException("이미 존재하는 nickName입니다");
            } else {
                return memberSignupCacheDto.getUuid();
            }
        }
        if (memberRepository.existsMemberByNickName(nickName)) {
            throw new AlreadyExistException("이미 존재하는 nickName입니다");
        }
        return signupCache.createNickNameCache(nickName, uuid);
    }

    @Override
    @Transactional
    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto, String uuid) throws AlreadyExistException {
        MemberSignupCacheDto memberSignupCacheDto = signupCache.isUsableEmail(memberSignupDto.getEmail(), uuid);
        if (memberSignupCacheDto.getUuid() == null && memberSignupCacheDto.isFlag()) {
            throw new AlreadyExistException("이미 존재하는 회원입니다");
        }
        memberSignupCacheDto = signupCache.isUsableNickName(memberSignupDto.getNickName(), memberSignupCacheDto.getUuid());
        if (memberSignupCacheDto.getUuid() == null && memberSignupCacheDto.isFlag()) {
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
        Member memberByEmailAndPw = memberRepository.findTop1ByEmailAndPwAndStatus(memberLoginDto.getEmail(), memberLoginDto.getPw(), Status.ACTIVE);
        if (memberByEmailAndPw == null)
            throw new FailedLoginException();
        return new MemberInfoDto(memberByEmailAndPw);
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
            postService.removePost(post.getId(), memberId);
        }
    }

    @Override
    @Transactional
    public MemberInfoDto searchMember(String nickName) throws EntityNotFoundException {
        Member member = memberRepository.findMemberByNickName(nickName);

        return new MemberInfoDto(member);
    }

    @Override
    public MemberFullInfoDto isValidPw(Long memberId, String password) throws EntityNotFoundException {
        Member member = memberRepository.findMemberByIdAndPw(memberId, password);
        if (member == null)
            throw new EntityNotFoundException("잘못된 비밀번호입니다");
        return new MemberFullInfoDto(member);
    }

    @Override
    public MemberInfoDto modifyMember(Long memberId, MemberUpdateDto memberUpdateDto) throws FileNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        member.setEmail(memberUpdateDto.getEmail());
        member.setNickName(memberUpdateDto.getEmail());
        member.setPhoneNumber(memberUpdateDto.getPhoneNumber());
        if (memberUpdateDto.getProfileImage() != null) {
            fileRepository.deleteFileByName(member.getProfileFileName());
            String uploadedFileName = fileRepository.createStoreFileName(memberUpdateDto.getProfileImage().getOriginalFilename());
            fileRepository.storeFile(memberUpdateDto.getProfileImage(), uploadedFileName);
            member.setProfileFileName(uploadedFileName);
        }
        return new MemberInfoDto(member);
    }

    @Override
    @Transactional
    public void following(Long memberId, Long followeeId) {
        Member follower = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 입니다."));
        Member followee = new Member();
        followee.setId(followeeId);
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        follow.setBestFriend(Status.INACTIVE);
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unFollowing(Long followerId, Long followeeId) {
        Follow target = followRepository.findById(new FollowId(followerId, followeeId)).orElseThrow(() -> new EntityNotFoundException("팔로우 관계가 없습니다."));
        System.out.println("target.getFollower() = " + target.getFollower());
        followRepository.delete(target);
    }

    @Override
    @Transactional
    public FollowingStatus getRelation(Long followerId, Long followeeId) {
        if (followerId != null && followerId != followeeId) {
            Optional<Follow> relation = followRepository.findById(new FollowId(followerId, followeeId));
            if (relation.isPresent()) {
                return FollowingStatus.UNFOLLOW;
            } else {
                return FollowingStatus.FOLLOW;
            }
        }
        return FollowingStatus.NONE;
    }

    @Override
    public MemberFullInfoDto searchMyInfo(Long memberId) throws EntityNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));
        return new MemberFullInfoDto(member);
    }


}
