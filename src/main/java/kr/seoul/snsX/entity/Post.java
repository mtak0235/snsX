package kr.seoul.snsX.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;

    private String author;

    @Lob
    private String content;

    @OneToMany(mappedBy = "post")
    List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<>();
}
