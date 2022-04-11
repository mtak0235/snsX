package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberSignupDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String pw;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String nickName;

    public Member toEntity() {
        return Member.builder()
                .pw(pw)
                .email(email)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .build();
    }

}