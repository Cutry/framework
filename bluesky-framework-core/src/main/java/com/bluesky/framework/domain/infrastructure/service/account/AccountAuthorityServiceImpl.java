package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.domain.model.account.AccountAuthorityRepository;
import com.bluesky.framework.domain.service.account.AccountAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountAuthorityServiceImpl implements AccountAuthorityService {
    @Autowired
    private AccountAuthorityRepository accountAuthorityRepository;

    @Override
    public void saveAuthorities(long accountId, List<AccountAuthority> accountAuthorityList) {
        //先删除原来的权限
        List<AccountAuthority> roleAuthorityListInDB = accountAuthorityRepository.findByAccountId(accountId);
        if (!roleAuthorityListInDB.isEmpty()) {
            accountAuthorityRepository.deleteByAccountId(accountId);
        }
        //新增没有任何权限情况下，则更新为没有任何权限
        if (!accountAuthorityList.isEmpty()) {
            accountAuthorityList.forEach(it -> it.setAccountId(accountId));
            accountAuthorityRepository.saveAuthorities(accountAuthorityList);
        }
    }
}