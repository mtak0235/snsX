package kr.seoul.snsX.service;

import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    private final PostService postService;

    @Autowired
    public PostServiceImplTest(PostService postService) {
        this.postService = postService;
    }

    @Test
    public void 게시물제거() {

    }

    @Test
    public void 없는게시물제거() {
        try {
            postService.removePost(1L);
        } catch (EntityNotFoundException e) {
        }
    }
}