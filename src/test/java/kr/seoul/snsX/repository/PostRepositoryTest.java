package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    private final PostRepository postRepository;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

}