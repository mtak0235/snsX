package kr.seoul.snsX.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    private String author;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
