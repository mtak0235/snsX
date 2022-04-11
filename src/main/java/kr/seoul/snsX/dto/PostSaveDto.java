package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class PostSaveDto {
    private Long postId;
    private Long memberId;
    private String content;
    private List<MultipartFile> imageFiles;
}
