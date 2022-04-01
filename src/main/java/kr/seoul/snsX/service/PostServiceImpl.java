package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.exception.FailImgSaveException;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.file.FileStore;
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
    private final FileStore fileStore;


    @Override
    public Post findPost(Long postId) {
        // 바로 get() 사용한 것 수정 요망
        return postRepository.findById(postId).get();
    }

    @Override
    @Transactional
    public Long uploadPost(PostSaveDto saveDto) throws IOException {
        if (saveDto.getImageFiles().size() > 3) {
            throw new ImageOverUploadedException("이미지 갯수 초과");
        }
        List<Image> storeImageFiles = fileStore.storeFiles(saveDto.getImageFiles());
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
        Post foundPost = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException());
        if (dto.getPostContent().equals(""))
            foundPost.setContent(dto.getPrevContent());
        else
            foundPost.setContent(dto.getPostContent());

        return foundPost.getId();
    }

    @Override
    @Transactional
    public void removePost(Long pk) throws EntityNotFoundException, FileNotFoundException {
        Post foundPost = postRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException());
        fileStore.deleteFiles(foundPost.getImages());
        postRepository.delete(foundPost);
    }
}
