package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberInfoDto {
    private String nickName;
    private Long memberId;

    public MemberInfoDto(Member member) {
        this.nickName = member.getNickName();
        this.memberId = member.getId();
    }

}
