package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserInfoDto {
    private String nickName;
    private Long memberId;

    public UserInfoDto(Member member) {
        this.nickName = member.getNickName();
        this.memberId = member.getId();
    }

}
