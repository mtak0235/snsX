package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Data;

@Data
public class MemberUpdateDto {
    private String email;
    private String nickName;
    private String profileImage;
    private String phoneNumber;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .profileFileName(profileImage)
                .phoneNumber(phoneNumber)
                .build();
    }
}
