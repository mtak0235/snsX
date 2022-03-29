package kr.seoul.snsX.controller;

import kr.seoul.snsX.*;
import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.repository.ImageRepository;
import kr.seoul.snsX.service.PostService;
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

    @GetMapping("/upload")
    public String newPost(@ModelAttribute PostSaveDto postSaveDto) {
        return "post_form";
    }

    @PostMapping("/upload")
    public String savePost(@ModelAttribute PostSaveDto postSaveDto) throws IOException {

        Long postId = postService.savePost(postSaveDto);

        return "redirect:/post/update/" + postId;
    }

    @GetMapping("/update/{postId}")
    public String post(@PathVariable Long postId, Model model) {

        Post post = postService.findPost(postId);
        model.addAttribute("post", post);

        return "post_update_form";
    }

    @PostMapping("/update/{postId}")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostUpdateDto postUpdateDto) throws IOException {

        postUpdateDto.setPostId(postId);
        postService.updatePost(postUpdateDto);

        return "redirect:/post/update/" + postId;
    }

}
