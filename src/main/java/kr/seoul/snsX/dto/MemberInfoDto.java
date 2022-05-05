package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Follow;
import kr.seoul.snsX.entity.Member;
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
    private List<Long> followee;

    public MemberInfoDto(Member member) {
        this.nickName = member.getNickName();
        this.memberId = member.getId();
        this.profileImage = member.getProfileFileName();
        if (!CollectionUtils.isEmpty(member.getFollowees())) {
            this.followee = member.getFollowees().stream().map(i-> i.getFollowee().getId()).collect(Collectors.toList());
            for (Follow t : member.getFollowees()) {
                System.out.println("t.getFollowee() = " + t.getFollowee());
                System.out.println("t.getFollowee().getId() = " + t.getFollowee().getId());
                System.out.println("t.getFollower() = " + t.getFollower());
                System.out.println("t.getFollower().getId() = " + t.getFollower().getId());
            }
        }
    }



}
