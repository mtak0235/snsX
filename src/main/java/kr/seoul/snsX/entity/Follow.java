package kr.seoul.snsX.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@IdClass(FollowId.class)
public class Follow {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", nullable = false)
    private Member follower;

    @Id
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "followee", nullable = false)
    private Member followee;

    @Enumerated(EnumType.STRING)
    private Status bestFriend;

    @Builder
    public Follow(Member follower, Member followee, Status bestFriend) {
        this.follower = follower;
        this.followee = followee;
        this.bestFriend = bestFriend;
    }
}
