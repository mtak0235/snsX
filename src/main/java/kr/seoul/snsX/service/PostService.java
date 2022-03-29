package kr.seoul.snsX.service;

import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.file.FileDelete;
import kr.seoul.snsX.file.FileStore;
import kr.seoul.snsX.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileStore fileStore;
    private final FileDelete fileDelete;

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
}
