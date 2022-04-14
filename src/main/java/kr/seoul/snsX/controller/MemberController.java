package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.failedLogin;
import kr.seoul.snsX.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
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
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);
        return "redirect:/post";
    }

    @PostMapping("/searchLost")
    public String searchLost(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "phoneNumber") String phoneNumber) {
        String email = memberService.searchLostMemberEmail(nickName, phoneNumber);
        return "";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") MemberLoginDto memberLoginDto) {
        return "signin";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute MemberLoginDto memberLoginDto, Model model) throws failedLogin {
        MemberInfoDto memberInfoDto = memberService.login(memberLoginDto);
        model.addAttribute("member", memberInfoDto);
        HttpSession session = request.getSession();
        session.setAttribute("member", memberInfoDto);
        session.setAttribute("first", 1);
        session.setAttribute("second", 2);
        return "post_feed_list";
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//
//    }
}
