package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.FailedLoginException;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.InputDataInvalidException;
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

    @GetMapping("/search/{nickName}")
    public String searchMemberPageForm(@PathVariable("nickName") String nickName, Model model) {
        MemberInfoDto memberInfoDto = memberService.searchMember(nickName);
        model.addAttribute("member", memberInfoDto);

        return "member_page_form";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") MemberLoginDto memberLoginDto) {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute("member") MemberLoginDto memberLoginDto, @RequestParam(defaultValue = "/post") String redirectURL) throws FailedLoginException {
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

    @GetMapping("/setting")
    public String setting() {
        return "setting";
    }

    @GetMapping("/withdraw")
    public String withdrawForm() {
        return "pw_verify";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpServletRequest request, @RequestParam(name = "password") String password) throws FileNotFoundException {
        memberService.removeMember(password, ((MemberInfoDto)request.getSession().getAttribute(SessionConst.LOGIN_MEMBER)).getMemberId());
        expireUserSession(request);
        return "redirect:/member/login";
    }
}
