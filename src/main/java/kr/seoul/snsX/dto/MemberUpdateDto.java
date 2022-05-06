package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class MemberUpdateDto {
    private String email;
    private String nickName;
    private MultipartFile profileImage;
    private String phoneNumber;

//    public Member toEntity() {
//        return Member.builder()
//                .email(email)
//                .nickName(nickName)
//                .profileFileName(profileImage)
//                .phoneNumber(phoneNumber)
//                .build();
//    }

}
