package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

}
