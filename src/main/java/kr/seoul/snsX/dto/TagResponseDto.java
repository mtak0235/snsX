package kr.seoul.snsX.dto;

import kr.seoul.snsX.entity.HashTag;
import lombok.Data;

@Data
public class TagResponseDto {
    private String tagName;
    private Long tagId;

    public TagResponseDto(HashTag tag) {
        this.tagName = tag.getName();
        this.tagId = tag.getId();
    }
}
