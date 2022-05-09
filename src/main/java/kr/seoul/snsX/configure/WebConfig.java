package kr.seoul.snsX.configure;

import kr.seoul.snsX.interceptor.LogInterceptor;
import kr.seoul.snsX.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/post", "/post/{postId:[0-9]+}", "/post/images/**", "/post/search/**", "/post/feed/**",
                        "/post/member_feed/**", "/member/signup/checkEmail/**", "/member/signup/checkNickName/**",
                        "/member/signup", "/member/searchLostMemberEmail", "/member/searchLostMemberPw",
                        "/member/login", "/member/search/**", "/search/", "/search", "/*.ico"
                );
    }
}
