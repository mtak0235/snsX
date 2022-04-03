package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.PostHashTag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostUpdateDto {

    private Long postId;

    private String prevContent;

    private String postContent;

}
