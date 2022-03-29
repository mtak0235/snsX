package kr.seoul.snsX.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

@Entity
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    private String author;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
