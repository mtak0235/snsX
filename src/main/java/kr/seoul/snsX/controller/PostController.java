package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Value("${file.dir}")
    private String fileDir;

    private final PostService postService;


    @GetMapping("/show")
    public String showFeedForm() {
//        FeedResponseDto feedResponseDto = postService.showPosts(lastPK, limit);
//        FeedResponseDto feed = new FeedResponseDto();
//        List<PostResponseDto> posts = new ArrayList<>();
//        posts.add(postService.getPost(1L));
//        posts.add(postService.getPost(2L));
//        posts.add(postService.getPost(3L));
//        feed.setPosts(posts);
//        model.addAttribute("posts", feed);

        return "list";
    }


    @GetMapping("/{postId}")
    public String showPostForm(@PathVariable Long postId, Model model) {
        PostResponseDto post = postService.getPost(postId);
        List<CommentResponseDto> comments = post.getComments();
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }
        model.addAttribute("user", post.getAuthor());
        model.addAttribute("post", post);
        return "post_result";
    }


    @GetMapping("/upload")
    public String savePostForm(@ModelAttribute PostSaveDto postSaveDto) {
        return "post_form";
    }

    @PostMapping("/upload")
    public String savePost(@ModelAttribute PostSaveDto postSaveDto) throws IOException, ImageOverUploadedException {
        Long postId = postService.uploadPost(postSaveDto);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/update/{postId}")
    public String updatePostForm(@PathVariable Long postId, Model model) {

        PostResponseDto post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "post_update_form";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute PostUpdateDto postUpdateDto) throws EntityNotFoundException, IOException {
        Long postId = postService.modifyPost(postUpdateDto);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam Long postId) throws EntityNotFoundException, FileNotFoundException {
        postService.removePost(postId);
        return "redirect:/";
    }

    @PostMapping("/{postId}/save-comment")
    public String saveComment(@PathVariable Long postId, @ModelAttribute CommentRequestDto requestDto) {
        postService.addComment(postId, requestDto);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/remove-comment")
    public String removeComment(@RequestParam(name = "postId") Long postId, @RequestParam(name = "commentId") Long commentId) {
        postService.removeComment(postId, commentId);
        return "redirect:/post/" + postId;
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource showImageForm(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileDir + filename);
    }

}
