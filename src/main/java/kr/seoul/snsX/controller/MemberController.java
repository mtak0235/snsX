package kr.seoul.snsX.controller;


import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.FailedLoginException;
import kr.seoul.snsX.service.MemberService;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
            , HttpServletRequest request) {
        if (result.hasErrors()) {
            return "signup";
        }
        MemberInfoDto userInfo = memberService.registerMember(member);
//        HttpSession session = request.getSession();
//        session.setAttribute("userInfo", userInfo);
        return "redirect:/member/login";
    }

    @PostMapping("/searchLost")
    public String searchLost(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "phoneNumber") String phoneNumber) {
        String email = memberService.searchLostMemberEmail(nickName, phoneNumber);
        return "";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") MemberLoginDto memberLoginDto) {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute("member") MemberLoginDto memberLoginDto, @RequestParam(defaultValue = "/") String redirectURL) throws FailedLoginException {
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
    public String withdraw(HttpServletRequest request, @RequestParam(name = "password") String password) {
        memberService.removeMember(password, ((MemberInfoDto)request.getSession().getAttribute(SessionConst.LOGIN_MEMBER)).getMemberId());
        expireUserSession(request);
        return "redirect:/member/login";
    }
}
