package kr.seoul.snsX.interceptor;

import kr.seoul.snsX.sessison.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            Cookie cookie = null;
            if (request.getMethod().equals("POST")){
                cookie = new Cookie(UUID.randomUUID().toString(), StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8));
                cookie.setPath(requestURI);
                response.addCookie(cookie);
            }
            response.sendRedirect("/member/login?redirectURL=" + requestURI + ((cookie != null)?("/" + cookie.getName()) : ""));
            return false;
        }
        return true;
    }
}
