package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    @Modifying
    @Query("DELETE FROM PostHashTag pht WHERE pht.post.id = ?1")
    void deleteByPostId(Long fk);
}
