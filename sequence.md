# isValidUserEmail

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

# isValidUserNickName

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

# 회원 등록

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>+c: email, nickName, pw,phoneNumber
c->>+s: registerMember(email, nickName, pw, phoneNumber)
s->>r: save(email, nickName, pw, phoneNumber)
r->>s: Optional<User>
alt 유저가 이미 존재하면
s->>c: throws alreadyExist("이미 존재하는 회원입니다")
c->>cli: throws alreadyExist("이미 존재하는 회원입니다")
else
s->>c: UserInfoDto
c->>cli: UserInfoDto
end

```

# deleteUser

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email, pw
c->>s: removeUser(email, pw)
s->>r: findUserByPw(email, pw)
r->>s: User
alt 유효하지 않은 비밀번호인 경우
s->>c: throws inValidAccess("잘못된 접근입니다")
c->>cli: throws inValidAccess("잘못된 접근입니다")
end
s->>r: deleteUser(pk)
r->>s: void
s->>c: void
c->>cli: void
```

# modifyUser(보류)

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: modifyUser(email)
s->>r: findUserBy(email, pw)
r->>s: User

```

# modifyUserPw(보류)

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: modifyUser(email)
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
s->>r: findUserByEmail(email, pw)
r->>s: User
alt 존재하지 않는 user인 경우
s->>c: throws failedlogin("로그인에 실패했습니다")
c->>cli: throws failedlogin("로그인에 실패했습니다")
end
s->>c: UserInfoDto
c->>cli: redirect:/main + UserInfoDto
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

# searchUser
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: nickName
c->>s: searchUser(nickName)
s->>r: findUserByNickName(nickName)
r->>s: List<foundUserDto(pk)>
s->>c: List<foundUserDto(pk)>
c->>cli: List<foundUserDto(pk)>
```

# searchLostUserId
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: nickName, phoneNumber
c->>s: searchLostUserId(nickName, phoneNumber)
s->>r: findUserIdByNickNameAndPhoneNumber(nickName, phoneNumber)
r->>s: foundUserId
alt "입력된 정보가 유효하지 않은 경우"
s->>c: throws inputDatainvalid("잘못된 정보입니다.")
c->>cli: throws inputDatainvalid("잘못된 정보입니다.")
end
s->>c: foundUserId
c->>cli: foundUserId
```

# searchLostUserPw(보류)
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: email
c->>s: searchLostUserPw(email)
s->>r: findUserPwByEmail(email)
r->>r: sendValidUrlToEmail(email)
cli->>c: validUrl
```

# searchUserPage
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: pk
c->>s: searchUserPage(pk)
s->>r: findUserByPk(pk)
r->>s: List<Object[]> (post_id, sumnail, nickName)
s->>c: foundUserInfoDto (Map<post_id, sumnail>, nickName)
c->>cli: foundUserInfoDto (Map<post_id, sumnail>, nickName)
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


### savePost
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant hashTagService
participant r as repository
participant l as Log

cli->>+c: 글, 이미지, 작성자
c->>+s: uploadPost(글, 이미지, 작성자)
s->>s: checkSize(이미지)
alt 이미지 사이즈가 규격외라면
s->>c: throw outOfSize()
c->>cli: "이미지 업로드 용량을 초과했습니다"
end
s->>r: saveImg(이미지)
alt 이미지 잘 저장되면
r->>s: List<파일 경로>
s->>r: saveOnDB(글, 작성자, List<파일 경로>, List<태그>)
alt db 저장에 실패하면
r->>s: throw failPostSave()
s->>c: throw failPostSave()
c->>cli: "save에 실패"
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
r->>s: id
s->>l: upload 성공
s->>c: id
c->>cli: "/" 
else
r->>s: throw failImgSave()
s->>c: throw failImgSave()
c->>cli: "save에 실패"

end
```

### delete post
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

### updatePostForm

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

### updatePost

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant hashTagService
participant r as repository
participant l as Log

cli->>c: pk, prevContent, postContent
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

### Add Comment

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: postId, content, commenter
c->>s: addComment(postId, content, commenter)
s->>r: findPostById(postId)
alt 게시물이 없으면
r->>s: throw EntityNotFoundException()
s->>c: throw EntityNotFoundException()
c->cli: 400 에러
end
s->>r: saveComment(postId, content, commenter)
r->>s: Comment(commentId, commenter, content)
s->>c: void
c->>cli: void
```

### Remove Comment

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
s->>r: deleteComment(postId, commentId)
r->>s: void
s->>c: void
c->>cli: void
```

### searchByTag

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: tag (String)
c->>s: getTagPosts(tag)
s->>r: findPostIdAndNameByTagName(tag)
r->>s: List<Object[]>
s->>c: List<TagFeedResponseDto>
c->>cli: List<TagFeedResponseDto>
```


<!-- 부적절하다는 요청 -->
### hide post
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

### restore post
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


### showPostForm
```mermaid
sequenceDiagram
actor op
participant c as controller
participant s as service
participant r as repository
participant l as Log

op->>c: postId
c->>s: getPost(postId)
s->>c: postResponseDto
c->>op: PostResponseDto, List<Comment>, author
```

### showFeed
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
s->>r: getPosts(pk, limit)
r->>s: List<Post>
s->>c: FeedResponseDto
c->>cli: FeedResponseDto
```

### showFeedForm
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

### Remove Post

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
