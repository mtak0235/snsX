package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class MemberSignupDto {

    @NotEmpty
    private String email;
    @NotEmpty
    @Pattern(regexp = "[\\w]{4,12}")
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
