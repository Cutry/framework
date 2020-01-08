package com.bluesky.framework.domain.factory.account;

import com.bluesky.framework.account.auth.AccountAuthority;

import java.util.List;


/**
 * 权限Factory
 */
public interface AuthorityFactory {

    /**
     * 查询用户的权限，如果用户未设置权限则从角色中来
     */
    List<AccountAuthority> findByAccountId(long accountId);
}