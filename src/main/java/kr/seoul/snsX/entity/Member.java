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

    @Column(unique = true)
    private String email;
    private String pw;
    private String phoneNumber;
    @Column(unique = true)
    private String nickName;
    private String profileFileName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? Status.ACTIVE : this.status;
    }

}
