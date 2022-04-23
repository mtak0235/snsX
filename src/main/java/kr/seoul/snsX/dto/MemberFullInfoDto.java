package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFullInfoDto {
    private String email;
    private String nickName;
    private String profileImage;
    private String phoneNumber;
    private String pw;


    public MemberFullInfoDto(Member member) {
        this.email = member.getEmail();
        this.nickName = member.getNickName();
        this.profileImage = member.getProfileFileName();
        this.phoneNumber = member.getPhoneNumber();
        this.pw = member.getPw();
    }
}
