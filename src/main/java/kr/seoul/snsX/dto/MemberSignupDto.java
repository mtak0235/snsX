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
    @Pattern(regexp = "[\\w]+@[\\w]+[.][a-z]+", message = "잘못된 형식입니다.")
    private String email;
    @NotEmpty
    @Pattern(regexp = "[\\w]{4,12}", message = "대소문자, 숫자 4~12만 가능합니다.")
    private String pw;
    @NotEmpty
    @Pattern(regexp = "[0-9]{11}", message = "잘못된 형식입니다.")
    private String phoneNumber;
    @NotEmpty
    @Pattern(regexp = "[\\w]{4,10}", message = "대소문자, 숫자 4~10만 가능합니다.")
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
