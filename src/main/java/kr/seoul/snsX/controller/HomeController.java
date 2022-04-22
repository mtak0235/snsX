package kr.seoul.snsX.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/search")
    public String searchMemberOrTag(HttpServletRequest request) {
        String searchWord = request.getParameter("searchWord");
        if (!searchWord.equals("") && searchWord.charAt(0) == '#')
            return "redirect:/post/search/" + searchWord;
        return "redirect:/member/search/" + searchWord;
    }
}
