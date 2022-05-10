package kr.seoul.snsX.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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
