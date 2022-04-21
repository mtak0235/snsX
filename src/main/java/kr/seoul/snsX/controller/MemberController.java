package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.InputDataInvalidException;
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

    @PostMapping("/searchLostMemberEmail")
    public String searchLostMemberEmail(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "phoneNumber") String phoneNumber, Model model) throws InputDataInvalidException {
        String email = memberService.searchLostMemberEmail(nickName, phoneNumber);
        model.addAttribute("search", "Email");
        model.addAttribute("value", email);
        return "search_lost_form";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("member") MemberLoginDto memberLoginDto) {
        return "signin";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute MemberLoginDto memberLoginDto) throws failedLogin {
        MemberInfoDto memberInfoDto = memberService.login(memberLoginDto);
        HttpSession session = request.getSession();
        session.setAttribute("memberInfo", memberInfoDto);

        return "redirect:/post/member_feed/"+ memberInfoDto.getMemberId();
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//
//    }
}
