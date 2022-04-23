package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(nativeQuery = true, value =
            "SELECT post_id, thumbnail_file_name FROM post WHERE post_id IN " +
                    "(SELECT post_id FROM post_hash_tag WHERE hashtag_id = :tagId) LIMIT :offset, :limit"
    )
    List<Object[]> findPostIdAndThumbnailFileNameByTagId(@Param("tagId") Long tagId, @Param("offset") Long offset, @Param("limit") Long limit);

    @Query(nativeQuery = true, value = "SELECT * FROM post p JOIN (SELECT post_id FROM post ORDER BY post_id DESC LIMIT :offset, :limit) pp ON pp.post_id=p.post_id ORDER BY p.post_id DESC")
    List<Post> findPosts(@Param("offset") Long offset, @Param("limit") Long limit);
}
