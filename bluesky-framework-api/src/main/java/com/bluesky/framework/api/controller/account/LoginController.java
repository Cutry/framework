package com.bluesky.framework.api.controller.account;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.boot.security.web.userdetails.CurrentUserDetails;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.api.controller.common.IPUtils;
import com.bluesky.framework.api.event.LogEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 平台后台登录
 */
@RestController
public class LoginController {
    @Value("${info.domain}")
    private String domain;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private RememberMeServices rememberMeServices;
    @Autowired
    private LogEventProcessor logEventProcessor;


    /**
     * 用户名登录
     * 用户名：目前是手机号
     */
    @PostMapping("/login/user_name")
    public DataResponse loginUserName(@RequestParam String userName, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
        DataResponse result = new DataResponse();
        List<Integer> roleType = Stream.of(new Integer[]{RoleType.GOVERNMENT.getCode(), RoleType.PLATFORM.getCode()}).collect(Collectors.toList());

        try {
            Account account = accountManager.loginUserName(userName, password, roleType, IPUtils.getIpAddr(request));
            this.handleLogin(account, request, response);
            account.setPassword(null);
            result.addData("account", account);
        } catch (Exception e) {
            if (Objects.equals("Z-100101", e.getMessage())) {
                result.setFalseAndMessage("用户名或密码错误");
            } else if (Objects.equals("Z-100102", e.getMessage())) {
                result.setFalseAndMessage("用户名或密码错误");
            } else if (Objects.equals("Z-100103", e.getMessage())) {
                result.setFalseAndMessage("您已被禁用,请联系管理员!");
            } else if (Objects.equals("Z-100104", e.getMessage())) {
                result.setFalseAndMessage("您已被锁定,请联系管理员!");
            } else if (Objects.equals("Z-100105", e.getMessage())) {
                result.setFalseAndMessage("用户名或密码错误");
            } else {
                result.setFalseAndMessage("系统错误");
            }
        }
        return result;
    }


    /**
     * 处理登录
     *
     * @param account  用户
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void handleLogin(Account account, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        CurrentUserDetails currentUserDetails = new CurrentUserDetails(account);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUserDetails, currentUserDetails.getPassword(), currentUserDetails.getAuthorities());
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
            @Override
            public void addCookie(Cookie cookie) {
                if (cookie.getDomain() == null) {
                    cookie.setDomain(domain);
                }
                cookie.setPath("/");
                super.addCookie(cookie);
            }
        };

        wrapper.addCookie(new Cookie("nickname", URLEncoder.encode(account.getMobile(), "UTF-8")));
        wrapper.addCookie(new Cookie("headImg", account.getHeadImg()));
        rememberMeServices.loginSuccess(request, wrapper, authentication);
    }


    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public DataResponse logout(@AuthenticationPrincipal Account account, HttpServletRequest request, HttpServletResponse response) {
        DataResponse result = new DataResponse();
        //清空cookie
        cancelCookie("remember-me", response);
        cancelCookie("nickname", response);
        cancelCookie("headImg", response);
        String ip = IPUtils.getIpAddr(request);
        if (account != null) {
            logEventProcessor.logoutEvent(account.getId(), ip);
        }
        return result;
    }


    /**
     * 查询当前登录用户信息
     *
     * @param account 当前登录账户
     */
    @GetMapping("/current_account")
    public DataResponse getCurrentAccount(@AuthenticationPrincipal Account account) {
        DataResponse result = new DataResponse();
        if (account != null) {
            account.setPassword(null);
        }
        result.addData("account", account);
        return result;
    }


    /**
     * 删除cookie
     *
     * @param cookieName cookie名称
     * @param response   响应
     */
    private void cancelCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }
}