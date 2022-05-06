package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.*;
import kr.seoul.snsX.exception.FailedLoginException;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.InputDataInvalidException;
import kr.seoul.snsX.exception.InvalidException;
import kr.seoul.snsX.service.MemberService;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.*;
import javax.validation.Valid;
import java.io.FileNotFoundException;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(@ModelAttribute(name = "member") MemberSignupDto member) {
        return "signup";
    }

    @PostMapping("/signup")
    public String save(@Valid @ModelAttribute(name = "member") MemberSignupDto member, BindingResult result
            , HttpServletRequest request, @CookieValue(name = "uuid", required = false) String uuid) throws AlreadyExistException {
        if (result.hasErrors()) {
            return "signup";
        }
        memberService.registerMember(member, uuid);
        return "redirect:/member/login";
    }

    @PostMapping("/signup/checkEmail")
    public String occupyMemberEmail(@RequestParam(name = "email") String email, @CookieValue(name = "uuid", required = false) String uuid, HttpServletResponse response) throws AlreadyExistException {
        String createdUuid = memberService.occupyEmail(email, uuid);
        if (uuid == null)
            response.addCookie(new Cookie("uuid", createdUuid));
        return "";
    }

    @PostMapping("/signup/checkNickName")
    public String occupyMemberNickName(@RequestParam(name = "nickName") String nickName, @CookieValue(name = "uuid", required = false) String uuid, HttpServletResponse response) throws AlreadyExistException {
        String createdUuid = memberService.occupyNickName(nickName, uuid);
        if (uuid == null)
            response.addCookie(new Cookie("uuid", createdUuid));
        return "";
    }

    @GetMapping("/searchLostMemberEmail")
    public String searchLostMemberEmailForm() {
        return "search_lost_email_form";
    }

    @PostMapping("/searchLostMemberEmail")
    public String searchLostMemberEmail(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "phoneNumber") String phoneNumber, Model model) throws InputDataInvalidException {
        String email = memberService.searchLostMemberEmail(nickName, phoneNumber);
        model.addAttribute("search", "Email");
        model.addAttribute("value", email);
        return "search_lost_form";
    }

    @GetMapping("/searchLostMemberPw")
    public String searchLostMemberPwForm() {
        return "search_lost_pw_form";
    }

    @PostMapping("/searchLostMemberPw")
    public String searchLostMemberPw(@RequestParam(name = "email") String email) throws InputDataInvalidException {
        memberService.searchLostMemberPw(email);
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") MemberLoginDto memberLoginDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto memberInfoDto, Model model) {
        if (memberInfoDto != null) {
            return "redirect:/post";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @ModelAttribute("member") MemberLoginDto memberLoginDto,
                        @RequestParam(defaultValue = "/post") String redirectURL)
            throws FailedLoginException {
        MemberInfoDto memberInfoDto = memberService.login(memberLoginDto);
        if (memberInfoDto == null) {
            return "login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberInfoDto);
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        expireUserSession(request);
        return "redirect:/member/login";
    }

    private void expireUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/verify")
    public String isValidPwForm(Model model) {
        model.addAttribute("path", "verify");
        return "pw_verify";
    }

    @PostMapping("/verify")
    public String isValidPw(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto loginMember, HttpServletRequest request, @RequestParam(name = "password") String password, Model model) throws InvalidException {
        MemberFullInfoDto memberFullInfoDto = memberService.isValidPw(loginMember.getMemberId(), password);
        if (memberFullInfoDto == null)
            return "redirect:/member/verify";
        model.addAttribute("loginMember", memberFullInfoDto);
        return "member_update_form";
    }

    @PostMapping("/update")
    public String modifyMemberForm(HttpServletRequest request, @ModelAttribute("member") MemberUpdateDto memberUpdateDto, Model model) {
        return "";
//        MemberInfoDto memberInfoDto = memberService.modifyMember(((MemberInfoDto)request.getSession().getAttribute(SessionConst.LOGIN_MEMBER)).getMemberId(), memberUpdateDto);
//        model.addAttribute("member", memberInfoDto);
//        return "";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(Model model) {
        model.addAttribute("path", "withdraw");
        return "pw_verify";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpServletRequest request, @RequestParam(name = "password") String password) throws FileNotFoundException {
        memberService.removeMember(password, ((MemberInfoDto) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER)).getMemberId());
        expireUserSession(request);
        return "redirect:/member/login";
    }

    @GetMapping("/search")
    public String searchMemberPageForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto loginMember,
                                       @RequestParam(name = "target") String nickName,
                                       Model model) {
        MemberInfoDto target = memberService.searchMember(nickName);
        if (loginMember != null) {
            loginMember.setFollowingStatus(memberService.getRelation(loginMember.getMemberId(), target.getMemberId()));
        }
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("member", target);
        return "member_page_form";
    }

    @ResponseBody
    @GetMapping("/follow")
    public String followMember(@RequestParam(value = "followee") Long followeeId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto loginMember) {
        memberService.following(loginMember.getMemberId(), followeeId);
        return "ok";
    }

    @ResponseBody
    @GetMapping("/unfollow")
    public String unfollowMember(@RequestParam(value = "followee", required = true) Long followeeId,
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberInfoDto loginMember) {
        System.out.println("MemberController.unfollowMember");
        memberService.unFollowing(loginMember.getMemberId(), followeeId);
        return "ok";
    }

    @GetMapping("/mypage")
    public String mypage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = true) MemberInfoDto loginMember, Model model) {
        model.addAttribute("loginMember", memberService.searchMyInfo(loginMember.getMemberId()));
        return "mypage";
    }
}
