package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Post;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto extends ThumbnailDto{

    private MemberInfoDto member;
    private String content;
    private List<String> images;
    private List<CommentResponseDto> comments;
    private String createdDate, modifiedDate;
    private String thumbnailFileName;
    private Long postId;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.member = new MemberInfoDto(post.getMember());
        this.content = post.getContent();
        this.images = post.getImages().stream().map(i -> i.getUploadedFilename()).collect(Collectors.toList());
        this.thumbnailFileName = "http://localhost:8080/post/images/" + post.getImages().get(0).getUploadedFilename();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
