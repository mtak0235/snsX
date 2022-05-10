package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.exception.FailedLoginException;

import java.io.FileNotFoundException;

public interface MemberService {
    public String occupyEmail(String email, String uuid);

    public String occupyNickName(String nickName, String uuid);

    public MemberInfoDto registerMember(MemberSignupDto memberSignupDto, String cacheId);

    public MemberInfoDto login(MemberLoginDto memberLoginDto) throws FailedLoginException;

    public String searchLostMemberEmail(String nickName, String phoneNumber);

    public void searchLostMemberPw(String email);

    public void removeMember(String password, Long memberId) throws FileNotFoundException;

    public MemberInfoDto searchMember(String nickName);

    public MemberFullInfoDto isValidPw(Long memberId, String password);

    public MemberInfoDto modifyMember(Long memberId, MemberUpdateDto memberUpdateDto) throws FileNotFoundException;

    public void following(Long memberId, Long followeeId);

    public void unFollowing(Long followerId, Long followeeId);

    public FollowingStatus getRelation(Long followerId, Long followeeId);

    public MemberFullInfoDto searchMyInfo(Long memberId);

}
