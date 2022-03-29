package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
