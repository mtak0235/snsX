package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

