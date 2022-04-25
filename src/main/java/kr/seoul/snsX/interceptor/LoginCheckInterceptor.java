package kr.seoul.snsX.interceptor;

import kr.seoul.snsX.cookie.CookieConst;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            if (request.getMethod().equals("POST")){
                log.info("미인증 사용자의 post 요청");
                Cookie cookie = null;
                if (request.getRequestURI().equals("/post/upload")) {
                    log.info("post/uplaoddddd");
                    cookie = new Cookie(CookieConst.PENDING_POST_CONTENT, StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8));
                    cookie.setPath(requestURI);
                    response.addCookie(cookie);
                } else if (request.getRequestURI().matches("\\/post\\/[0-9]*\\/save-comment")) {
                    log.info("미인증 사용자의post/save-comment");
                    cookie = new Cookie(CookieConst.PENDING_COMMENT_CONTENT, StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8));
                    cookie.setPath(requestURI);
                    System.out.println("cookie.getValue() = " + cookie.getValue());
                    response.addCookie(cookie);
                }
            }
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
//        if (request.getCookies() != null) {
//            Cookie comment = Arrays.stream(request.getCookies())
//                    .filter(cookie -> cookie.getName().equals(CookieConst.PENDING_COMMENT_CONTENT))
//                    .findAny()
//                    .orElse(null);
//            if (comment != null) {
//                System.out.println("comment = " + comment.getValue());
//                //comment.getValue를 request body에 넣어야 함.
//                comment.setMaxAge(0);
//                response.addCookie(comment);
//            }
//        }
        return true;
    }
}
