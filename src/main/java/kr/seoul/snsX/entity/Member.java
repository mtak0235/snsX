package kr.seoul.snsX.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("'no-image.jpeg'")
    private String profileFileName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.REMOVE)
    private List<Follow> followees = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? Status.ACTIVE : this.status;
        this.profileFileName = this.profileFileName == null ? "no-image.jpeg" : this.profileFileName;
    }

}
