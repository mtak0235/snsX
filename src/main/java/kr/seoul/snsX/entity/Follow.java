package kr.seoul.snsX.entity;

import javax.persistence.*;

@Entity
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
}
