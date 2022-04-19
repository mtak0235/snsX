package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.failedLogin;
import kr.seoul.snsX.service.MemberService;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    public String login(HttpServletRequest request, @ModelAttribute("member") MemberLoginDto memberLoginDto) throws failedLogin {
        MemberInfoDto memberInfoDto = memberService.login(memberLoginDto);
        if (memberInfoDto == null) {
            return "login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberInfoDto);

        return "redirect:/post/member_feed/"+ memberInfoDto.getMemberId();
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//
//    }
}
