package kr.seoul.snsX.repository;

import kr.seoul.snsX.dto.ResponseTagFeedDto;
import kr.seoul.snsX.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT " +
                "new ResponseTagFeedDto(p.post_id, i.filename)" +
            "FROM PostHashTag pht" +
            "JOIN Post p" +
            "ON p.post_id = pht.post_id" +
            "JOIN" +
                "(SELECT" +
                    "post_id, ANY_VALUE(uploaded_filename) filename" +
                "FROM image" +
                "GROUP BY post_id) i" +
            "ON i.post_id = pht.post_id" +
            "JOIN HashTag h" +
            "ON (h.hashtag_id = pht.hashtag_id AND h.name = ?1)"
    )
    List<ResponseTagFeedDto> findPostIdAndFilenameByTagName(String tagName);
}
