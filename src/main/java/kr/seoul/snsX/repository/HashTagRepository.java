package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    HashTag findByName(String name);
}
