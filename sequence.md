# isValidMemberEmail

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>+c: email
c->>+s: isValid(email)
s->>r: findEmailByEmail(email)
r->>s: boolean
alt email이 존재하는 경우
s->>c: throws alreadyExist("이미 존재하는 email입니다")
c->>cli: boolean
end
```

# isValidMemberNickName

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>+c: nickName
c->>+s: isValid(nickName)
s->>r: findNickNameByNickName(nickName)
r->>s: boolean
alt nickName이 존재하는 경우
s->>c: throws alreadyExist("이미 존재하는 nickName입니다")
c->>cli: "이미 존재하는 nickName입니다"
end
```

# memberUpload

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>+c: MemberSignupDto(email, nickName, pw,phoneNumber)
c->>+s: registerMember(MemberSignupDto)
s->>r: save(email, nickName, pw, phoneNumber)
r->>s: Member
alt 유저가 이미 존재하면
s->>c: throws alreadyExist("이미 존재하는 회원입니다")
c->>cli: throws alreadyExist("이미 존재하는 회원입니다")
else
s->>c: void
c->>cli: redirect:/member/login
end

```

# deleteMember

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key), pw
c->>r: findEmailByKey(key)
r->>c: email
alt: key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>s: removeMember(email, pw)
s->>r: findMemberByPwAndEmail(email, pw)
r->>s: Member
alt 유효하지 않은 비밀번호인 경우
s->>c: throws inValidAccess("잘못된 접근입니다")
c->>cli: throws inValidAccess("잘못된 접근입니다")
end
s->>r: member.setActive(false)
s->>r: deletePost(List<Post>)
r->>s: void
s->>c: void
c->>c: expire(cookie)
c->>cli: void
```

# settingMemberForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key)
c->>r: existsCookieByKey(key)
r->>c: boolean
alt: key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>cli: void
```

# isValidPwForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key)
c->>r: existsCookieByKey(key)
r->>c: boolean
alt: key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>cli: void
```

# isValidPw
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key), pw
c->>r: findMemberIdByKey(key)
r->>c: memberId
alt: Key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>s: isValidPw(memberId, pw)
s->>r: existsMemberByMemberIdAndPw(memberId, pw)
r->>s: boolean
alt: pw가 유효하지 않은 경우
s->>c: throw EntityNotFoundException()
c->>cli: throw EntityNotFoundException()
end
s->>c: void
c->>cli: void
```

# modifyMemberForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key), memberId
c->>r: findEmailByKey(key)
r->>c: email
alt: key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>
```

# modifyMember

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: cookie(key)
c->>r: findEmailByKey(key)
r->>c: email
alt: key가 유효하지 않은 경우
c->>cli: redirect:/member/login
end
c->>s: modifyMember(email)
s->>r: findMemberBy(email, pw)
r->>s: Member
s->>c: MemberInfoDto(Member)
c->>cli: redirect:/member/{memberId} + MemberInfoDto
```

# modifyMemberPw(보류)

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: modifyMember(email)
s->>s: sendValidUrlToEmail(email)
cli->>c: validUrl
```

# login
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email, pw
c->>s: login(email, pw)
s->>r: findMemberByEmail(email, pw)
r->>s: Member
alt 존재하지 않는 member인 경우
s->>c: throws failedlogin("로그인에 실패했습니다")
c->>cli: throws failedlogin("로그인에 실패했습니다")
end
s->>c: MemberInfoDto
c->>cli: redirect:/main + MemberInfoDto
```

# logout(보류)
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: 
c->>cli: redirect:/main
```

# searchMember
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: nickName
c->>s: searchMember(nickName)
s->>r: findMemberByNickName(nickName)
r->>s: List<MemberInfoDto(pk, nickName, profileFileName)>
s->>c: List<MemberInfoDto>
c->>cli: List<MemberInfoDto>
```

# searchLostMemberEmail
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: nickName, phoneNumber
c->>s: searchLostMemberEmail(nickName, phoneNumber)
s->>r: findMemberEmailByNickNameAndPhoneNumber(nickName, phoneNumber)
r->>s: email
alt "입력된 정보가 유효하지 않은 경우"
s->>c: throws inputDataInvalid("잘못된 정보입니다.")
c->>cli: throws inputDataInvalid("잘못된 정보입니다.")
end
s->>c: email
c->>cli: email
```

# searchLostMemberPw(보류)
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: searchLostMemberPw(email)
s->>r: findMemberPwByEmail(email)
r->>r: sendValidUrlToEmail(email)
cli->>c: validUrl
```

# searchMemberPageForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: memberId
c->>s: searchMember(memberId)
s->>r: findNickNameAndProfileFileNameById(memberId)
r->>s: Object[] (nickName, profileFileName)
s->>c: MemberInfoDto(Object[])
c->>cli: MemberInfoDto
```

# searchMemberPage
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: memberId, offset, limit
c->>s: searchMemberPage(memberId, offset, limit)
s->>r: findMemberPosts(memberId, offset, limit)
r->>s: List<Object[]> (post_id, thumbnailFileName)
s->>c: List<thumbnailDto(post_id, thumbnailFileName)>
c->>cli: List<thumbnailDto>
```


# modifyProfileImage(보류)
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

```


# savePost
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant hashTagService
participant r as repository
participant l as Log

cli->>+c: PostSaveDto(글, 이미지, memberId)
c->>+s: uploadPost(PostSaveDto)
s->>s: checkSize(이미지)
alt 이미지 사이즈가 규격외라면
s->>c: throw outOfSize()
c->>cli: "이미지 업로드 용량을 초과했습니다"
end
loop: uploadedFileName을 가진 Image 객체 List 생성
s->>r: createStoreFileName(originalFileName)
r->>s: uploadedFileName
end
s->>r: storeFile(List<MultipartFile>, List<Image>)
alt: 이미지 저장에 실패하면
r->>s: throw failImgSave();
s->>c: throw failImgSave();
c->>cli: throw failImgSave();
end
r->>s: void
s->>r: save(Post(글, 작성자, List<파일 경로>, List<태그>))
s->>hashTagService: storePostHashTags(Post)
alt if numOfPostHashTag > 0
hashTagService->>r: deleteByPostId(postId)
end
hashTagService->>hashTagService: parsingHashTag(postContent)
hashTagService->>hashTagService: getHashTagList(Set<String> hashTagName)
loop foundHashTag exist
hashTagService->>r: save(postHashTag) 
end
hashTagService->>s: List<PostHashTag>
r->>s: getId(postId)
s->>l: upload 성공
s->>c: postId
c->>cli: "/"
```

# delete post
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: postId
c->>s: removePost(postId)
s->>r: findById(postId)
alt 게시물이 없으면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
s->>r: deleteFiles(image)
alt 게시물이 없으면
r->>s: throw FileNotFoundException()
s->>c: throw FileNotFoundException()
c->cli: 400 에러
end
s->>r: delete(foundPost)
s->>c: void
c->>cli: void
```

# updatePostForm

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log
cli->>c: postId
c->>s: getPostToModify(postId)
s->>r: getPost(postId)
alt 없는 post를 수정 요청하면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
r->>s: Post(images, content, author, dateTime)
s->>c: PostResponseDto
c->>cli: PostResponseDto
```

# updatePost

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant hashTagService
participant r as repository
participant l as Log

cli->>c: PostUpdateDto(pk, prevContent, postContent)
c->>s: modifyPost(PostUpdateDto)
s->>r: findById(pk)
alt 없는 post를 조회 요청하면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: throw EntityNotFoundException()
end
s->>hashTagService: storePostHashTags(prev)
alt if numOfPostHashTag > 0
hashTagService->>r: deleteByPostId(postId)
end
hashTagService->>hashTagService: parsingHashTag(postContent)
hashTagService->>hashTagService: getHashTagList(Set<String> hashTagName)
loop foundHashTag exist
hashTagService->>r: save(postHashTag) 
end
hashTagService->>s: List<PostHashTag>
s->>c: postId
c->>cli: redirect:/post/postId
```

# Add Comment

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: postId, memberId, CommentRequestDto(content)
c->>s: addComment(postId, memberId, CommentRequestDto)
s->>r: findPostById(postId)
alt 게시물이 없으면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
r->>s: Post
s->>r: findMemberById(memberId)
alt 게시물이 없으면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
r->>s: Member
s->>r: save(Comment)
r->>s: void
s->>c: void
c->>cli: void
```

# Remove Comment

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: postId, commentId
c->>s: removeComment(postId, commentId)
s->>r: findPostById(postId)
alt 게시물이 없으면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
s->>r: delete(postId, commentId)
r->>s: void
s->>c: void
c->>cli: void
```

# searchByTagForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: tagName
c->>s: getTagByTagName(tagName)
s->>r: findByName(tagName)
r->>s: HashTag
s->>c: TagResponseDto(HashTag)
c->>cli: TagResponseDto
```


# searchByTag

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: tagId, offset, limit
c->>s: getTagPosts(tagId, offset, limit)
s->>r: findPostIdAndThumbnailFileNameByTagId(tagId, offset, limit)
r->>s: List<Object[]>
s->>c: List<ThumbnailDto(post_id, thumbnailFileName)>
c->>cli: List<ThumbnailDto>

```


<!-- 부적절하다는 요청 -->
# hide post(보류)
```mermaid
sequenceDiagram
actor op
participant c as controller
participant s as service
participant r as repository
participant l as Log

op->>c: postId
c->>s: hidePost(postId)
s->>r: findById(postId)
r->>r: activePost(invisible)
r->>s: postId




```

# restore post(보류)
```mermaid
sequenceDiagram
actor op
participant c as controller
participant s as service
participant r as repository
participant l as Log

op->>c: postId
c->>s: hidePost(postId)
s->>r: findById(postId)
r->>r: changeStatus(invisible)
r->>s: postId



```


# showPostForm
```mermaid
sequenceDiagram
actor op
participant c as controller
participant s as service
participant r as repository
participant l as Log

op->>c: postId
c->>s: getPost(postId)
s->>r: findById(postId)
r->>s: Post
s->>c: postResponseDto(Post's info)
c->>op: PostResponseDto
```

# showFeed
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log
# 매번 새로운 것을 보여줘야함
cli->>c: offset,limit
c->>s: showPosts(offset, limit)
s->>r: findPosts(pk, limit)
r->>s: List<Post>
s->>c: List<ThumbnailDto(postId, thumbnailFileName)>
c->>cli: List<ThumbnailDto>
```

# showFeedForm
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: void
c->>cli: void
```

# Remove Post

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: pk
c->>s: removePost(pk)
s->>r: removePost(pk)
r->>r: findPostByIdInDB(pk)
r->>r: insertPostInTrashcan(Post)
alt 없는 post를 삭제 요청하면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
r->>s: void
s->>c: void
c->>cli: void
```
