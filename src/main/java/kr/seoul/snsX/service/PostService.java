package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.*;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponseDto getPost(Long postId);

    Long uploadPost(PostSaveDto saveDto) throws IOException;

    Long modifyPost(PostUpdateDto dto) throws IOException, EntityNotFoundException;

    void removePost(Long pk) throws EntityNotFoundException, FileNotFoundException;

    void addComment(Long postId, CommentRequestDto requestDto);

    void removeComment(Long postId, Long commentId);

    List<PostResponseDto> showPosts(Long offset, Long limit);

    List<TagFeedResponseDto> getTagPosts(String tag);

}
