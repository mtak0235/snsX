package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseTagFeedDto {

    private Long postId;

    private String fileName;

    public ResponseTagFeedDto(Long postId, String fileName) {
        this.postId = postId;
        this.fileName = fileName;
    }
}
