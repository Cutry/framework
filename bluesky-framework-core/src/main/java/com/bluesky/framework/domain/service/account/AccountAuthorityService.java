package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.auth.AccountAuthority;

import java.util.List;

public interface AccountAuthorityService {

    /**
     * 保存账户权限
     */
    void saveAuthorities(long accountId, List<AccountAuthority> accountAuthorityList);
}