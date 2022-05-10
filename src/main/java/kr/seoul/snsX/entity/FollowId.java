package kr.seoul.snsX.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowId implements Serializable {
    private Long follower;
    private Long followee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId that = (FollowId) o;
        return follower.equals(that.follower) &&
                followee.equals(that.followee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followee);
    }
}
