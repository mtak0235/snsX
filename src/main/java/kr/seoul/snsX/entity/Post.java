package kr.seoul.snsX.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    private String thumbnailFileName;

    @Lob
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostHashTag> postHashTags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @Builder
    public Post(String thumbnailFileName, String content, List<Image> images, List<Comment> comments, List<PostHashTag> postHashTags, Member member) {
        this.thumbnailFileName = thumbnailFileName;
        this.content = content;
        this.images = images;
        this.comments = comments;
        this.postHashTags = postHashTags;
        this.member = member;
    }
}
