package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Follow;
import kr.seoul.snsX.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class MemberInfoDto {
    private String nickName;
    private Long memberId;
    private String profileImage;
    private List<Follow> followee;
    private List<Follow> followers;
    private FollowingStatus followingStatus;

    private String pw;
    private String email;
    private String phoneNumber;

    public MemberInfoDto(Member member) {
        this.nickName = member.getNickName();
        this.memberId = member.getId();
        this.profileImage = member.getProfileFileName();
        this.followee = member.getFollowees();
        this.followingStatus = FollowingStatus.NONE;

        this.pw = member.getPw();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();

    }
}
