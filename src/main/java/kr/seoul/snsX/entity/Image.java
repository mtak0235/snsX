package kr.seoul.snsX.entity;

import javax.persistence.*;

@Entity
public class Image {

    @Id @GeneratedValue
    @Column(name = "IMAGE_ID")
    private Long id;

    private String originalFilename;

    private String uploadedFilename;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

}
