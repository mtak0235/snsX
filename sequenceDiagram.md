### Upload Post
```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>+c: 글, 이미지, 작성자
c->>+s: uploadPost(글, 이미지, 작성자)
s->>s: checkSize(이미지)
alt 이미지 사이즈가 규격외라면
s->>c: throw outOfSize()
c->>cli: "이미지 업로드 용량을 초과했습니다"
end
s->>s: parseTag(글)
s->>s: [List<태그>]
s->>r: AreTagsPresent(List<태그>)
alt 태그 있으면 
r->>s: List<Tag> 
else 태그 없으면
r->>r: createTag(tag)
r->>s: Tag
s->>s: append(Tag)
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

### Show Posts

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log
# 매번 새로운 것을 보여줘야함
cli->>c: [pk of last post],limit
c->>s: showPosts([pk], limit)
s->>r: getPosts(pk, limit)
loop List<Post>
s->>r: getImages(filePath)
alt 파일 서버에서 이미지 파일이 날아간 경우
s->>s: exchangeBrokenPost2NewPost(postId)
end
end
r->>s: List<Post>(List<img>, author, content, List<Comment>), LastPK
s->>c: List<Post>, LastPK
c->>cli: List<Post>, LastPK
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
r->>s: throw NotExistPost()
s->>c: throw NotExistPost()
c->cli: "존재하지 않는 게시물입니다"
end
r->>s: void
s->>c: void
c->>cli: void
```

### Get Post

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
r->>s: throw NotExistPost()
s->>c: throw NotExistPost()
c->cli: "존재하지 않는 게시물입니다"
end
r->>s: Post(images, content, author, dateTime)
s->>c: Post
c->>cli: Post
```

### Modify Post

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: pk, content, List<img>
c->>s: modifyPost(pk, content, List<img>)
s->>r: findPostById(pk)
alt 없는 post를 조회 요청하면
r->>s: throw NotExistPost()
s->>c: throw NotExistPost()
c->cli: "게시물 수정에 실패했습니다."
end
r->>s: images to remove
s->>r: deleteImage(images to remove)
alt 없는 post를 삭제 요청하면
r->>s: throw NotExistImage()
s->>c: throw NotExistImage()
c->cli: "게시물 수정에 실패했습니다."
end
s->>r: saveImage(List<img>)
alt 파일 저장에 실패하면
r->>s: throw ImgFileSaveException()
s->>c: throw ImgFileSaveException()
c->>cli: "게시물 수정에 실패했습니다."
end
r->>s: filePaths
s->>+r: updatePost(pk, content, filePaths)
r->>-s: pk
s->>c: void
c->>cli: void
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
r->>s: throw NotExistPost()
s->>c: throw NotExistPost()
c->cli: "존재하지 않는 게시물입니다"
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
r->>s: throw NotExistPost()
s->>c: throw NotExistPost()
c->cli: "존재하지 않는 게시물입니다"
end
s->>r: deleteComment(postId, commentId)
r->>s: void
s->>c: void
c->>cli: void
```

### search by Tag

```mermaid
sequenceDiagram
actor cli
participant c as controller
participant s as service
participant r as repository
participant l as Log

cli->>c: 태그
c->>s: getTagPosts(태그)
s->>r: findPostsIdAndFrontFilePathByTag(태그)
r->>r: findImg(filePath)
r->>s: [Map<postId, 와꾸 이미지>]
s->>s: getPostUrl(postId)
s->>c: [Map<postUrl, 와꾸 이미지>]
c->>cli: [Map<postUrl, 와꾸 이미지>]
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

### delete post