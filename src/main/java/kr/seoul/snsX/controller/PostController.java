package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.PostSaveDto;
import kr.seoul.snsX.dto.PostUpdateDto;
import kr.seoul.snsX.entity.Post;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    public String savePost(@ModelAttribute PostSaveDto postSaveDto) throws IOException, ImageOverUploadedException {
        Long postId = postService.uploadPost(postSaveDto);
        return "redirect:/post/update/" + postId;
    }

    @GetMapping("/update/{postId}")
    public String post(@PathVariable Long postId, Model model) {

        Post post = postService.findPost(postId);
        model.addAttribute("post", post);

        return "post_update_form";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute PostUpdateDto postUpdateDto) throws EntityNotFoundException, IOException {

        log.error(String.valueOf(postUpdateDto.getImageFiles().size()) + String.valueOf(postUpdateDto.getImageFiles() == null));
        MultipartFile multipartFile = postUpdateDto.getImageFiles().get(0);
        log.error(String.valueOf(multipartFile == null));
        Long postId = postService.modifyPost(postUpdateDto);

        return "redirect:/post/update/" + postId;
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam Long postId) throws EntityNotFoundException, FileNotFoundException {
        postService.removePost(postId);
        return "redirect:/";
    }


}
