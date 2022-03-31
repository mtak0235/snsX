package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    private Long id;
    private String author;
    private String content;
    private List<Image> images;
    private List<CommentResponseDto> comments;
    private String createdDate, modifiedDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.content = post.getContent();
        this.images = post.getImages();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}