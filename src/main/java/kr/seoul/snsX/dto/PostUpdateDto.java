package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostUpdateDto {

    private Long postId;

    private String postContent;

    private List<MultipartFile> imageFiles = new ArrayList<>();

}
