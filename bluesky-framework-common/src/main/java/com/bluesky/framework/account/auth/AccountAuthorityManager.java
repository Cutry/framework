package com.bluesky.framework.account.auth;

import java.util.List;

public interface AccountAuthorityManager {

    /**
     * 保存账户权限
     */
    void saveAuthorities(Long accountId, List<AccountAuthority> accountAuthorityList);

    /**
     * 查询用户的权限
     */
    List<AccountAuthority> findByAccountId(Long accountId);
}