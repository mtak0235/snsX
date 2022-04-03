package kr.seoul.snsX.service;

import kr.seoul.snsX.entity.HashTag;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.entity.PostHashTag;
import kr.seoul.snsX.repository.HashTagRepository;
import kr.seoul.snsX.repository.PostHashTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashTagServiceImpl implements HashTagService {

    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;

    private HashTag createHashTag(String tagName) {
        HashTag hashTag = new HashTag();
        hashTag.setName(tagName);
        hashTagRepository.save(hashTag);
        return hashTag;
    }

    @Override
    public Set<String> parsingHashTag(String content) {
        Set<String> foundTag = new HashSet<>();
        Pattern pattern = Pattern.compile("#[^# ]+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find())
            foundTag.add(matcher.group());
        return foundTag;
    }

    @Override
    @Transactional
    public List<HashTag> getHashTagList(Set<String> tagNames) {
        List<HashTag> foundHashTags = new ArrayList<>();
        for (String tagName : tagNames) {
            HashTag foundHashTag = hashTagRepository.findByName(tagName);
            if (foundHashTag == null)
                foundHashTag = createHashTag(tagName);
            foundHashTags.add(foundHashTag);
        }
        return foundHashTags;
    }

    @Override
    @Transactional
    public List<PostHashTag> storePostHashTags(Post post) {
        List<PostHashTag> storedPostHashTags = new ArrayList<>();
        if (post.getPostHashTags().size() != 0)
            postHashTagRepository.deleteByPostId(post.getId());
        List<HashTag> foundHashTags = getHashTagList(parsingHashTag(post.getContent()));
        for (HashTag foundHashTag : foundHashTags) {
            PostHashTag postHashTag = new PostHashTag();
            postHashTag.setPost(post);
            postHashTag.setHashTag(foundHashTag);
            postHashTagRepository.save(postHashTag);
            storedPostHashTags.add(postHashTag);
        }
        return storedPostHashTags;
    }

}
