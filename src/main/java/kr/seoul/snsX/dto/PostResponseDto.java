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
    private List<String> images;
    private List<CommentResponseDto> comments;
    private String createdDate, modifiedDate;
    private Integer numOfPostHashTag;

    public PostResponseDto(Post post, String fileDir) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.content = post.getContent();
        this.images = post.getImages().stream().map(i -> i.getUploadedFilename()).collect(Collectors.toList());
        System.out.println("images = " + images);
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.numOfPostHashTag = post.getPostHashTags().size();
    }
}
