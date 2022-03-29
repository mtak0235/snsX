package kr.seoul.snsX.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    private String originalFilename;
    private String uploadedFilename;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public Image(String originalFilename, String uploadedFilename) {
        this.originalFilename = originalFilename;
        this.uploadedFilename = uploadedFilename;
    }

    public Image() {

    }
}
