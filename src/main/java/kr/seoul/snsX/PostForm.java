package kr.seoul.snsX;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostForm {
    private Long postId;
    private String author;
    private String content;
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;
}
