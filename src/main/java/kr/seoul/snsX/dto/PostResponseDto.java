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
    private UserInfoDto user;
    private String content;
    private List<String> images;
    private List<CommentResponseDto> comments;
    private String createdDate, modifiedDate;

    private String name;
    private String img;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.user = new UserInfoDto(post.getMember());
        this.content = post.getContent();
        this.images = post.getImages().stream().map(i -> i.getUploadedFilename()).collect(Collectors.toList());
        System.out.println("images = " + images);
        this.img = "http://localhost:8080/post/images/" + post.getImages().get(0).getUploadedFilename();
        this.name = this.user.getNickName();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
