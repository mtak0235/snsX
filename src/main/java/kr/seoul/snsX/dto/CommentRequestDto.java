package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.Comment;
import kr.seoul.snsX.entity.Member;
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
    private String content;

}
