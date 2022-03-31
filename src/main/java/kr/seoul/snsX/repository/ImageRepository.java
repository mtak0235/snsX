package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
