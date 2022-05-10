package kr.seoul.snsX.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/search")
    public String
    searchMemberOrTag(HttpServletRequest request) throws UnsupportedEncodingException {
        String searchWord = request.getParameter("searchWord");
        if (!searchWord.equals("") && searchWord.charAt(0) == '#') {
            return "redirect:/post/search?" + "target=" + URLEncoder.encode(searchWord.substring(1), "utf-8");
        }
        return "redirect:/member/search?" + "target=" + URLEncoder.encode(searchWord, "utf-8");
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
