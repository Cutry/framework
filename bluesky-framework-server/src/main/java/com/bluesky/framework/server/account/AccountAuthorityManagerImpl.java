package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.account.auth.AccountAuthorityManager;
import com.bluesky.framework.domain.model.account.AccountAuthorityRepository;
import com.bluesky.framework.domain.service.account.AccountAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@DubboService
public class AccountAuthorityManagerImpl implements AccountAuthorityManager {

    @Autowired
    private AccountAuthorityService accountAuthorityService;
    @Autowired
    private AccountAuthorityRepository accountAuthorityRepository;

    @Override
    public void saveAuthorities(Long accountId, List<AccountAuthority> accountAuthorityList) {
        accountAuthorityService.saveAuthorities(accountId, accountAuthorityList);
    }

    @Override
    public List<AccountAuthority> findByAccountId(Long accountId) {
        return accountAuthorityRepository.findByAccountId(accountId);
    }
}