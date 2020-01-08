package com.bluesky.framework.account.boot.security.web;

import com.bluesky.framework.account.boot.security.web.userdetails.CurrentAccountControllerAdvice;
import com.bluesky.framework.account.boot.security.web.userdetails.UserDetailsServiceImpl;
import com.bluesky.framework.boot.FrameworkAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;

import static com.bluesky.framework.account.boot.security.web.FrameworkWebSecurityConfigurerAdapter.rememberMeAppKey;

/**
 * @author liyang
 */
@Configuration
@AutoConfigureAfter(FrameworkAutoConfiguration.class)
public class SecurityWebAutoConfiguration {
    @Value("${info.domain}")
    private String domain;

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    UserDetailsService customUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(PermissionEvaluator.class)
    PermissionEvaluator permissionEvaluator() {
        return new PermissionEvaluatorImpl();
    }

    @Bean
    CurrentAccountControllerAdvice currentAccountControllerAdvice() {
        return new CurrentAccountControllerAdvice();
    }

    @Bean
    @ConditionalOnMissingBean(RememberMeServices.class)
    RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeAppKey, customUserDetailsService());
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setUseSecureCookie(false);
        rememberMeServices.setCookieDomain(domain);
        //1200为20分钟
        rememberMeServices.setTokenValiditySeconds(1200);
        return rememberMeServices;
    }
}
