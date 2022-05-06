package kr.seoul.snsX.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/search")
    public String
    searchMemberOrTag(HttpServletRequest request) {
        String searchWord = request.getParameter("searchWord");
        if (!searchWord.equals("") && searchWord.charAt(0) == '#') {
            System.out.println("searchWord = " + searchWord);
            return "redirect:/post/search?" + "target=" + searchWord.substring(1);
        }
        return "redirect:/member/search?" + "target=" + searchWord;
    }

    @ResponseBody
    @RequestMapping("/AAA")
    public String AAA() {
//        return "redirect:/BBB";
        return BBB();
    }


    @ResponseBody
    @RequestMapping("/BBB")
    public String BBB() {
        return "BBB";
    }

}
