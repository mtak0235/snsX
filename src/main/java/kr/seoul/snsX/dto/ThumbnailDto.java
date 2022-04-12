package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ThumbnailDto {

    protected Long postId;
    protected String thumbnailFileName;

    public ThumbnailDto(Long postId, String thumbnailFileName) {
        this.postId = postId;
        this.thumbnailFileName = thumbnailFileName;
    }
}
