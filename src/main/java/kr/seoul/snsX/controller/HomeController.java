package kr.seoul.snsX.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/search")
    public String searchMemberOrTag(HttpServletRequest request) {
        String searchWord = request.getParameter("searchWord");
        if (searchWord != "" && searchWord.charAt(0) == '#')
            return "redirect:/post/search/" + searchWord;
        return "redirect:/member/search/" + searchWord;
    }
}
