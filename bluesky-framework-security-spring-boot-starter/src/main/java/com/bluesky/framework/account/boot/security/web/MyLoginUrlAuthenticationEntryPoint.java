package com.bluesky.framework.account.boot.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 未登录或者登陆过期或者登陆cookie错误运行此代码
 *
 * @author liyang
 */
public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private String loginUrl;

    public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl, String loginUrl) {
        super(loginFormUrl);
        this.loginUrl = loginUrl;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 未登录情况，则返回未登录的标志
        Map<String, Object> result = new HashMap<>(4);
        result.put("success", "UNLOGIN");
        result.put("code", "401");
        result.put("redirectUrl", loginUrl);
        result.put("message", "您未登录或登录超时!");
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
