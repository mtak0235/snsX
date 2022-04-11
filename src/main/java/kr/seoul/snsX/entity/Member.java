package kr.seoul.snsX.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    private String email;
    private String pw;
    private String phoneNumber;
    private String nickName;

    @OneToMany
    private List<Post> posts = new ArrayList<>();

}
