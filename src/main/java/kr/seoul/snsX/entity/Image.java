package kr.seoul.snsX.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    private String originalFilename;
    private String uploadedFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    public Image() {
    }

    public Image(String originalFilename, String uploadedFilename) {
        this.originalFilename = originalFilename;
        this.uploadedFilename = uploadedFilename;
    }

}
