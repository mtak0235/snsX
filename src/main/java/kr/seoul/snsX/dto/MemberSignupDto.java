package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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
                .profileFileName("no-image.png")
                .pw(pw)
                .email(email)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .build();
    }

}
