package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Comment;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Data
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String createdDate;
    private String modifiedDate;
    private Long postId;
    private UserInfoDto user;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getContent();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = comment.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.postId = comment.getPost().getId();
        this.user = new UserInfoDto(comment.getMember());
    }
}