package com.bluesky.framework.account.boot.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;

/**
 * @author liyang
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public abstract class FrameworkWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    protected RememberMeServices rememberMeServices;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${info.account.login.url}")
    private String loginUrl;
    @Value("${info.account.logout.url}")
    private String logoutUrl;

    /**
     * white urls for static resources
     */
    protected String[] whiteUrls = new String[]{"/login/**", "/webjars/**", "/static/**", "/static/css/**", "/static/images/**", "/static/js/**", "/jsondoc", "/jsondoc-ui.html", "**/*.css", "/webjars/**", "**/*.js", "**/*.map", "*.html", "/health", "/metrics"};

    /**
     * remember me key for salt
     */
    protected static String rememberMeAppKey = "bluesky_data_007";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(whiteUrls).permitAll()
                .antMatchers(loginUrl).permitAll()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin()
                .loginPage(loginUrl)
                .permitAll()
                .and()
                .logout()
                .logoutUrl(logoutUrl)
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .rememberMe().key(rememberMeAppKey).rememberMeServices(rememberMeServices)
                .and()
                .exceptionHandling().authenticationEntryPoint(myLoginUrlAuthenticationEntryPoint())
                .accessDeniedHandler(restAccessDeniedHandler());
        internalConfigure(http);
    }


    /**
     * internal configure for http security, main: antMatchers("/home").authenticated()
     *
     * @param http http security
     * @throws Exception 配置异常
     */
    protected abstract void internalConfigure(HttpSecurity http) throws Exception;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    @ConditionalOnMissingBean(MyLoginUrlAuthenticationEntryPoint.class)
    MyLoginUrlAuthenticationEntryPoint myLoginUrlAuthenticationEntryPoint() {
        return new MyLoginUrlAuthenticationEntryPoint("/", loginUrl);
    }


    @Bean
    @ConditionalOnMissingBean(RestAccessDeniedHandler.class)
    RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(MyWebSecurityExpressionHandler.class)
    MyWebSecurityExpressionHandler myWebSecurityExpressionHandler() {
        return new MyWebSecurityExpressionHandler();
    }
}
