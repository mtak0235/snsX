package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class MemberUpdateDto {
    @NotEmpty(message = "User's email cannot be empty.")
    private String email;
    @NotEmpty(message = "User's nickName cannot be empty.")
    private String nickName;
    @NotNull(message = "User's profileImage cannot be empty.")
    private MultipartFile profileImage;
    @NotEmpty(message = "User's phoneNumber cannot be empty.")
    private String phoneNumber;
    @NotEmpty(message = "User's pw cannot be empty.")
    private String pw;


//    public Member toEntity() {
//        return Member.builder()
//                .email(email)
//                .nickName(nickName)
//                .profileFileName(profileImage)
//                .phoneNumber(phoneNumber)
//                .build();
//    }

}
