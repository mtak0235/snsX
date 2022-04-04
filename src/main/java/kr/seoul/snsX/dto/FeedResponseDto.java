package kr.seoul.snsX.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeedResponseDto {
    Long lastPK;
    List<PostResponseDto> posts;
}
