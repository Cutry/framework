package com.bluesky.framework.account.boot.security.web.userdetails;

import com.bluesky.framework.account.account.Account;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author liyang
 */
@ControllerAdvice
public class CurrentAccountControllerAdvice {

    @ModelAttribute("currentAccount")
    Account getAccountAccount(Authentication authentication) {
        return authentication != null ? (CurrentUserDetails) authentication.getPrincipal() : null;
    }
}
