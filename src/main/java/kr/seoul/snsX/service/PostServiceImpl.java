package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.entity.Comment;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Member;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.exception.FailImgSaveException;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;
    private final HashTagService hashTagService;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public PostResponseDto getPost(Long postId) throws EntityNotFoundException{
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));
        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public Long uploadPost(PostSaveDto saveDto) throws IOException, ImageOverUploadedException {
        checkImageSize(saveDto);
        List<Image> storeImageFiles = new ArrayList<>();
        for (MultipartFile file : saveDto.getImageFiles()) {
            Image image = new Image(file.getOriginalFilename(), fileRepository.createStoreFileName(file.getOriginalFilename()));
            storeImageFiles.add(image);
        }
        fileRepository.storeFiles(saveDto.getImageFiles(), storeImageFiles);
        Post post = new Post();
        post.setContent(saveDto.getContent());
        post.setImages(storeImageFiles);
        post.setThumbnailFileName(storeImageFiles.get(0).getUploadedFilename());
        post.setMember(memberRepository.getById(saveDto.getMemberId()));
        post.setPostHashTags(hashTagService.storePostHashTags(post));
        postRepository.save(post);
        for (Image storeImageFile : storeImageFiles) {
            storeImageFile.setPost(post);
            imageRepository.save(storeImageFile);
        }
        return post.getId();
    }

    private void checkImageSize(PostSaveDto saveDto)throws ImageOverUploadedException {
        if (saveDto.getImageFiles().size() > 3) {
            throw new ImageOverUploadedException("이미지 갯수 초과");
        }
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
        prevPost.setPostHashTags(hashTagService.storePostHashTags(prevPost));
        return prevPost.getId();
    }

    @Override
    @Transactional
    public void removePost(Long postId, Long memberId) throws EntityNotFoundException, FileNotFoundException {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException());
        if (havePermission(memberId, foundPost.getMember().getId())) {
            fileRepository.deleteFiles(foundPost.getImages());
            postRepository.delete(foundPost);
        } else {
            throw new InvalidException();
        }
    }

    @Override
    public void addComment(Long postId, Long memberId, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new EntityNotFoundException("댓글 쓰기 실패: 해당 게시물이 존재하지 않습니다." + postId));
        Member commenter = memberRepository.findById(memberId).orElseThrow(()->
                new EntityNotFoundException("댓글 쓰기 실패: 회원 번호가 올바르지 않습니다."));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMember(commenter);
        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long postId, Long commentId, Long memberId) throws EntityNotFoundException, InvalidException {
        postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException());
        if (havePermission(memberId, comment.getMember().getId()))
            commentRepository.delete(comment);
        else
            throw new InvalidException();
    }

    private boolean havePermission(Long memberId, Long makerId) {
        if (memberId == makerId)
            return true;
        else
            return false;
    }

    @Override
    @Transactional
    public List<ThumbnailDto> showPosts(Long cursor, Long limit) {
        List<Post> posts;
        if (cursor == -1)
            posts = postRepository.findFirstFollowingPosts(limit);
        else
            posts = postRepository.findFollowingPosts(cursor, limit);
        List<ThumbnailDto> thumbnailDtos = new ArrayList<>();
        for (Post p : posts) {
            thumbnailDtos.add(new ThumbnailDto(p.getId(), p.getThumbnailFileName()));
        }
        return thumbnailDtos;
    }

    @Override
    public List<ThumbnailDto> getTagPosts(Long tagId, Long cursor, Long limit) throws EntityNotFoundException {

        List<Object[]> result = postRepository.findPostIdAndThumbnailFileNameByTagId(tagId, cursor, limit);
        List<ThumbnailDto> convertedResult = new ArrayList<>();
        for (Object[] r : result) {
            convertedResult.add(new ThumbnailDto(((BigInteger)r[0]).longValue(), (String)r[1]));
        }
        return convertedResult;
    }

    @Override
    public List<ThumbnailDto> findMemberPosts(Long memberId, Long offset, Long limit) {
        List<Post> posts = postRepository.findMemberPosts(memberId, offset, limit);
        List<ThumbnailDto> thumbnailDtos = new ArrayList<>();
        for (Post p : posts) {
            thumbnailDtos.add(new ThumbnailDto(p.getId(), p.getThumbnailFileName()));
        }
        return thumbnailDtos;
    }

}
