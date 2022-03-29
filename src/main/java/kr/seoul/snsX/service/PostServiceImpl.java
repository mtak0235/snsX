package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.file.FileDelete;
import kr.seoul.snsX.file.FileStore;
import kr.seoul.snsX.repository.ImageRepository;
import kr.seoul.snsX.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;
    private final FileDelete fileDelete;
    //private final BackupPostRepository backupPostRepository;


    @Override
    public Post findPost(Long postId) {
        // 바로 get() 사용한 것 수정 요망
        return postRepository.findById(postId).get();
    }

    @Override
    @Transactional
    public Long savePost(PostSaveDto saveDto) throws IOException {
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
    public void updatePost(PostUpdateDto dto) throws IOException {
        Optional<Post> foundPost = postRepository.findById(dto.getPostId());

        Post post = foundPost.get();
        post.setContent(dto.getPostContent());

        fileDelete.deleteFiles(post.getImages());

        post.setImages(fileStore.storeFiles(dto.getImageFiles()));
        for (Image img : post.getImages()) {
            img.setPost(post);
        }
    }

    @Override
    public void removePost(Long pk) throws EntityNotFoundException {
        Post findPost = postRepository.getById(pk);
        //backupPostRepository.save(findPost);
        postRepository.delete(findPost);
    }
}
