package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(nativeQuery = true, value =
            "SELECT post_id, thumbnail_file_name " +
                    "FROM post " +
                    "INNER JOIN (SELECT post_id FROM post LIMIT :offset, :limit) pp ON pp.post_id = post.post_id " +
                    "WHERE post_id IN " +
                    "(SELECT post_id FROM post_hash_tag WHERE hash_tag = " +
                    "(SELECT hashtag_id FROM hash_tag WHERE name = :tagId))"
    )
    List<Object[]> findPostIdAndThumbnailFileNameByTagId(@Param("tagId") Long tagId, @Param("offset") Long offset, @Param("limit") Long limit);

    @Query(nativeQuery = true, value = "SELECT * FROM post p JOIN (SELECT post_id FROM post ORDER BY post_id DESC LIMIT :offset, :limit) pp ON pp.post_id=p.post_id ORDER BY p.post_id DESC")
    List<Post> findPosts(@Param("offset") Long offset, @Param("limit") Long limit);
}
