package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Post;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PostService {

    Post findPost(Long postId);

    Long uploadPost(PostSaveDto saveDto) throws IOException;

    Long modifyPost(PostUpdateDto dto) throws IOException, EntityNotFoundException;

    void removePost(Long pk) throws EntityNotFoundException, FileNotFoundException;
}
