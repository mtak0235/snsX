package kr.seoul.snsX.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class PostHashTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTHASHTAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HASHTAG_ID")
    private HashTag hashTag;
}
