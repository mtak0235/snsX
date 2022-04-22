package kr.seoul.snsX.interceptor;

import kr.seoul.snsX.cookie.CookieConst;
import kr.seoul.snsX.sessison.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            if (request.getRequestURI().equals("/post/upload") && request.getMethod().equals("POST")){

            }
            Cookie cookie = new Cookie(CookieConst.PENDING_POST_CONTENT, request.get);
            response.addCookie(cookie);

            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
