package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
