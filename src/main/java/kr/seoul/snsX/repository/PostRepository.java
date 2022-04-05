package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true, value =
            "SELECT p.post_id, i.filename " +
            "FROM post_hash_tag pht " +
            "JOIN post p " +
            "ON p.post_id = pht.post_id " +
            "JOIN " +
                "(SELECT " +
                    "post_id, ANY_VALUE(uploaded_filename) filename " +
                "FROM image " +
                "GROUP BY post_id) i " +
            "ON i.post_id = pht.post_id " +
            "JOIN hash_tag h " +
            "ON (h.hashtag_id = pht.hashtag_id AND h.name = :tagName)"
    )
    List<Object[]> findPostIdAndFilenameByTagName(@Param("tagName") String tagName);

    @Query(nativeQuery = true, value = "SELECT * FROM post p JOIN (SELECT post_id FROM post ORDER BY post_id DESC LIMIT :offset, :limit) pp ON pp.post_id=p.post_id ORDER BY p.post_id DESC")
    List<Post> findPosts(@Param("offset") Long offset, @Param("limit") Long limit);
}
