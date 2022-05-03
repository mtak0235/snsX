package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.exception.ImageOverUploadedException;
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.service.HashTagService;
import kr.seoul.snsX.service.PostService;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Value("${file.dir}")
    public String fileDir;

    private final PostService postService;
    private final HashTagService hashTagService;

    @GetMapping("/upload")
    public String savePostForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) MemberInfoDto memberInfoDto, Model model) {
        model.addAttribute("loginMember", memberInfoDto);
        return "post_form";
    }

    @PostMapping("/upload")
    public String savePost(@ModelAttribute PostSaveDto postSaveDto,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto) throws IOException, ImageOverUploadedException {
        postSaveDto.setMemberId(memberInfoDto.getMemberId());
        Long postId = postService.uploadPost(postSaveDto);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/update/{postId}")
    public String updatePostForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto, @PathVariable Long postId, Model model) {

        PostResponseDto post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("loginMember", memberInfoDto);
        return "post_update_form";
    }

    @PostMapping("/update")
    public String updatePost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto, @ModelAttribute PostUpdateDto postUpdateDto, Model model) throws EntityNotFoundException, IOException {
        Long postId = postService.modifyPost(postUpdateDto);
        model.addAttribute("loginMember", memberInfoDto);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam Long postId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto
    ) throws EntityNotFoundException, FileNotFoundException {
        postService.removePost(postId, memberInfoDto.getMemberId());
        return "redirect:/post";
    }

    @GetMapping("/{postId}")
    public String showPostForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto, @PathVariable("postId") Long postId, Model model) {
        PostResponseDto post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("loginMember", memberInfoDto);
        return "post_result";
    }

    @GetMapping("/{postId}/save-comment/{prev}")
    public String saveCommentForm(@PathVariable Long prev, @PathVariable Long postId) {
        return "redirect:/post/" + postId + "/" + prev;
    }

    @PostMapping("/{postId}/save-comment")
    public String saveComment(@PathVariable Long postId, @ModelAttribute CommentRequestDto requestDto,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto) {
        postService.addComment(postId, memberInfoDto.getMemberId(), requestDto);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/remove-comment")
    public String removeComment(@RequestParam(name = "postId") Long postId, @RequestParam(name = "commentId") Long commentId,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER) MemberInfoDto memberInfoDto)
    throws EntityNotFoundException, InvalidException {
        postService.removeComment(postId, commentId, memberInfoDto.getMemberId());
        return "redirect:/post/" + postId;
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource showImageForm(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileDir + filename);
    }

    @GetMapping("/search/{tag}")
    public String searchByTagForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto userInfo, @PathVariable("tag") String tag, Model model) throws EntityNotFoundException {
        TagResponseDto tagResponseDto = hashTagService.getTagByTagName("#" + tag);
        model.addAttribute("tag", tagResponseDto);
        model.addAttribute("loginMember", userInfo);
        return "tag_feed_form";
    }

    @ResponseBody
    @GetMapping("/search/{tagId}/{cursor}/{limit}")
    public List<ThumbnailDto> searchByTag(@PathVariable("tagId") Long tagId, @PathVariable("cursor") Long cursor, @PathVariable Long limit) {
        List<ThumbnailDto> result = postService.getTagPosts(tagId, cursor, limit);
        return result;
    }

    @GetMapping
    public String showFeedForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto userInfo, Model model) {
        model.addAttribute("loginMember", userInfo);
        return "post_feed_form";
    }

    @ResponseBody
    @GetMapping("/feed/{cursor}/{limit}")
    public List<ThumbnailDto> showFeed (@PathVariable Long cursor, @PathVariable("limit") Long limit){
        List<ThumbnailDto> result = postService.showPosts(cursor, limit);
        return result;
    }

    @ResponseBody
    @GetMapping("/search/member/{memberId}/{offset}/{limit}")
    public List<ThumbnailDto> searchMemberPage(@PathVariable Long memberId, @PathVariable Long offset, @PathVariable Long limit) {
        List<ThumbnailDto> result = postService.findMemberPosts(memberId, offset, limit);
        return result;
    }

//    @GetMapping("/member_feed/{memberId}")
//    public String memberFeedForm(HttpServletRequest request, @PathVariable Long memberId, Model model) {
//        HttpSession session = request.getSession(false);
//        MemberInfoDto memberInfo = (MemberInfoDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        model.addAttribute("loginMember", memberInfo);
//        return "post_feed_form";
//    }
}
