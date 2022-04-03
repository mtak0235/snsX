package kr.seoul.snsX.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class HashTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HASHTAG_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "hashTag", cascade = CascadeType.REMOVE)
    List<PostHashTag> postHashTags = new ArrayList<>();
}
