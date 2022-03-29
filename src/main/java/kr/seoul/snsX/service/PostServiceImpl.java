package kr.seoul.snsX.service;

import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    //private final BackupPostRepository backupPostRepository;

    @Override
    public void removePost(Long pk) throws EntityNotFoundException {
        Post findPost = postRepository.getById(pk);
        //backupPostRepository.save(findPost);
        postRepository.delete(findPost);
    }
}
