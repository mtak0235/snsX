package kr.seoul.snsX.controller;

import kr.seoul.snsX.*;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final EntityManager em;

    @GetMapping("/upload")
    public String newPost(@ModelAttribute PostForm postForm) {
        return "post_form";
    }

    @Transactional
    @PostMapping("/upload")
    public String savePost(@ModelAttribute PostForm postForm, RedirectAttributes redirectAttributes) throws IOException {
//        Image attachFile = postService.storeFile(postForm.getAttachFile());
        List<Image> storeImageFiles = postService.storeFiles(postForm.getImageFiles());

//데이터베이스에 저장
        Post post = new Post();
        post.setAuthor(postForm.getAuthor());
        post.setContent(postForm.getContent());
//        post.setAttachFile(attachFile);
        post.setImages(storeImageFiles);
        postRepository.save(post);
        for (Image storeImageFile : storeImageFiles) {
            storeImageFile.setPost(post);
            imageRepository.save(storeImageFile);
        }


        redirectAttributes.addAttribute("itemId", post.getId());

//        return "redirect:/post/{postId}";
        return "redirect:/post/upload";
    }


//    @GetMapping("/post/{postId}")
//    public post(@PathVariable Long id, Model model) {
//
//    }
}
