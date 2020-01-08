package com.bluesky.framework.api.controller.common;

import com.bluesky.framework.account.boot.security.web.FrameworkWebSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * web security config
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class FrameworkSecurityConfigurerAdapter extends FrameworkWebSecurityConfigurerAdapter {

    @Override
    protected void internalConfigure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
        authorizeRequests
                .antMatchers("/config/**").permitAll()
                .antMatchers("/setting/page/**").hasRole("ADMIN");
        this.antMatchersO(authorizeRequests, "/regions/*/delete").hasRole("ADMIN");
        this.antMatchersO(authorizeRequests, "/regions/update_system_level", "/regions/add", "/regions/*/update").hasRole("ADMIN");
        this.antMatchersO(authorizeRequests, "/regions/**").hasRole("PLATFORM");
        authorizeRequests.antMatchers("/account/auth/**").hasRole("PLATFORM");
        this.antMatchersO(authorizeRequests, "/role/auth/**", "/role/add", "/role/*/delete", "/role/*/update_name").hasRole("ADMIN");
        this.antMatchersO(authorizeRequests, "/role/**").hasRole("PLATFORM");
        this.antMatchersO(authorizeRequests, "/organization/add", "/organization/update", "/organization/delete").hasRole("ADMIN");
        this.antMatchersO(authorizeRequests, "/organization/current_organization").hasRole("GOVERNMENT");
        this.antMatchersO(authorizeRequests, "/organization/**").hasRole("PLATFORM");
        this.antMatchersO(authorizeRequests, "/account/**").hasRole("PLATFORM");
        this.antMatchersO(authorizeRequests, "/current_account").permitAll();
        this.antMatchersO(authorizeRequests, "/logout").permitAll();
        this.antMatchersO(authorizeRequests, "/get_login_log/", "/get_operate_log").hasRole("PLATFORM");
    }

    /**
     * 扩展security的antMatchers写法
     * <p>
     * security中，如果写了.antMatchers("/logout").hasRole("PLATFORM")，发现虽然"/logout"被限制了权限，但是"/logout/”却绕过了权限
     * 这样很危险，但是每次同样一个url写两遍，这样比较繁琐。下面的扩展工具方法减少一些繁琐
     */
    public org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl antMatchersO(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests, String... str) {
        List<String> list = new ArrayList<>();
        for (String it : str) {
            // 把末尾的斜杠去掉
            if (it.endsWith("/")) {
                String url = it.replaceAll("/*$", "");
                list.add(url);
                list.add(url + "/");
            } else {
                list.add(it);
                list.add(it + "/");
            }
        }
        return authorizeRequests.antMatchers(list.toArray(new String[0]));
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(whiteUrls).antMatchers("/login/user_name");
    }
}