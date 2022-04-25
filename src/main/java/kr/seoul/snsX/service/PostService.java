package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.exception.InvalidException;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponseDto getPost(Long postId);

    Long uploadPost(PostSaveDto saveDto) throws IOException;

    Long modifyPost(PostUpdateDto dto) throws IOException, EntityNotFoundException;

    void removePost(Long pk, Long memberId) throws EntityNotFoundException, FileNotFoundException;

    void addComment(Long postId,Long memberId, CommentRequestDto requestDto);

    void removeComment(Long postId, Long commentId, Long memberId) throws EntityNotFoundException, InvalidException;

    List<ThumbnailDto> showPosts(Long offset, Long limit);

    List<ThumbnailDto> getTagPosts(Long tagId, Long offset, Long limit);

}
