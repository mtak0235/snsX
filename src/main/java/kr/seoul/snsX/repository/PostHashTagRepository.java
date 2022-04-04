package kr.seoul.snsX.repository;

import kr.seoul.snsX.dto.ResponseTagFeedDto;
import kr.seoul.snsX.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    @Modifying
    @Query("DELETE FROM PostHashTag pht WHERE pht.post.id = ?1")
    void deleteByPostId(Long fk);
}
