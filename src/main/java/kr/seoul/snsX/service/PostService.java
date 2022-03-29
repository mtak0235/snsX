package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Post;

import java.io.IOException;

public interface PostService {

    Post findPost(Long postId);

    Long savePost(PostSaveDto saveDto) throws IOException;

    void updatePost(PostUpdateDto dto) throws IOException;

    void removePost(Long pk);

}
