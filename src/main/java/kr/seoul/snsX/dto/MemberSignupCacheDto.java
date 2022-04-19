package kr.seoul.snsX.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSignupCacheDto {
    private String uuid;
    private boolean flag;

    public MemberSignupCacheDto(String uuid, boolean flag) {
        this.uuid = uuid;
        this.flag = flag;
    }
}
