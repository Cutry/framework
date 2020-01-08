package com.bluesky.framework.account.boot.security.web.userdetails;

import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * service
 *
 * @author liyang
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public CurrentUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountManager accountManager = (AccountManager) applicationContext.getBean("accountManager");
        Account account = accountManager.findOne(Long.parseLong(username));
        if (account == null) {
            throw new UsernameNotFoundException("user can not found");
        }
        return new CurrentUserDetails(account);
    }

}


