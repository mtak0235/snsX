package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.CommentRequestDto;
import kr.seoul.snsX.dto.PostResponseDto;
import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Comment;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.exception.FailImgSaveException;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.repository.CommentRepository;
import kr.seoul.snsX.repository.FileRepository;
import kr.seoul.snsX.repository.ImageRepository;
import kr.seoul.snsX.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public PostResponseDto findPost(Long postId) {
        // 바로 get() 사용한 것 수정 요망
        return new PostResponseDto(postRepository.findById(postId).get());
    }

    @Override
    @Transactional
    public Long uploadPost(PostSaveDto saveDto) throws IOException {
        if (saveDto.getImageFiles().size() > 3) {
            throw new ImageOverUploadedException("이미지 갯수 초과");
        }
        List<Image> storeImageFiles = fileRepository.storeFiles(saveDto.getImageFiles());
        Post post = new Post();
        post.setAuthor(saveDto.getAuthor());
        post.setContent(saveDto.getContent());
        post.setImages(storeImageFiles);
        postRepository.save(post);
        for (Image storeImageFile : storeImageFiles) {
            storeImageFile.setPost(post);
            imageRepository.save(storeImageFile);
        }
        return post.getId();
    }

    @Override
    @Transactional
    public Long modifyPost(PostUpdateDto dto) throws EntityNotFoundException, FileNotFoundException,
            IllegalArgumentException, FailImgSaveException {
        Post prevPost = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException());

        if (dto.getPostContent().equals(""))
            prevPost.setContent(dto.getPrevContent());
        else
            prevPost.setContent(dto.getPostContent());

        return prevPost.getId();
    }

    @Override
    @Transactional
    public void removePost(Long pk) throws EntityNotFoundException, FileNotFoundException {
        Post foundPost = postRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException());
        fileRepository.deleteFiles(foundPost.getImages());
        postRepository.delete(foundPost);
    }

    @Override
    public void addComment(Long postId, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시무링 존재하지 않습니다." + postId));
        requestDto.setPost(post);
        Comment comment = requestDto.toEntity();
        commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException());
        commentRepository.delete(comment);
    }
}
