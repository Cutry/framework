package com.bluesky.framework.domain.model.account;

import com.bluesky.framework.account.auth.AccountAuthority;

import java.util.List;

public interface AccountAuthorityRepository {

    /**
     * 保存权限信息
     */
    void saveAuthorities(List<AccountAuthority> accountAuthorityList);

    /**
     * 根据账户查询权限
     */
    List<AccountAuthority> findByAccountId(long accountId);

    /**
     * 根据账户删除
     *
     * @param accountId 账户ID
     */
    void deleteByAccountId(long accountId);

    /**
     * 根据账户、操作删除，当角色删除某个权限时，用户相应的删除
     *
     * @param accountIds 账户ID
     */
    void deleteByAccountIdsAndOperationId(List<Long> accountIds, long operationId);
}