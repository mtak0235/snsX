package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Comment;
import kr.seoul.snsX.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    private Long id;
    private String content;
    private String commenter;
    private Post post;

    public Comment toEntity() {
        Comment comment = Comment.builder()
                .id(id)
                .content(content)
                .author(commenter)
                .post(post)
                .build();
        return comment;
    }
}
