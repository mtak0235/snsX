package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Follow;
import kr.seoul.snsX.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

}
