package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.PostHashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostUpdateDto {

    private Long postId;

    private String prevContent;

    private String postContent;

    @Builder
    public PostUpdateDto(Long postId, String prevContent, String postContent) {
        this.postId = postId;
        this.prevContent = prevContent;
        this.postContent = postContent;
    }
}
