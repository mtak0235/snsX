package kr.seoul.snsX.service;

import kr.seoul.snsX.entity.HashTag;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.entity.PostHashTag;

import java.util.List;
import java.util.Set;

public interface HashTagService {

    Set<String> parsingHashTag(String content);

    List<HashTag> findHashTags(Set<String> tagNames);

    List<PostHashTag> storePostHashTags(Post post, Integer numOfPostHashTag);

}
