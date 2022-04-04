package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TagFeedResponseDto {

    private Long id;

    private String fileName;

    public TagFeedResponseDto(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }
}
