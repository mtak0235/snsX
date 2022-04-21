package kr.seoul.snsX.controller;

import kr.seoul.snsX.dto.MemberLoginDto;
import kr.seoul.snsX.dto.MemberSignupDto;
import kr.seoul.snsX.dto.MemberInfoDto;
import kr.seoul.snsX.exception.AlreadyExistException;
import kr.seoul.snsX.exception.failedLogin;
import kr.seoul.snsX.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
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
