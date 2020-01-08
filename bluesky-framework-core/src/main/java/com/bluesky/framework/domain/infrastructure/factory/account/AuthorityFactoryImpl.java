package com.bluesky.framework.domain.infrastructure.factory.account;

import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.domain.factory.account.AuthorityFactory;
import com.bluesky.framework.domain.model.account.AccountAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AuthorityFactoryImpl implements AuthorityFactory {
    @Autowired
    private AccountAuthorityRepository accountAuthorityRepository;

    @Override
    public List<AccountAuthority> findByAccountId(long accountId) {
        return accountAuthorityRepository.findByAccountId(accountId);
        /*val accountAuthorityList = accountAuthorityRepository.findByAccountId(accountId)
        //没有配置个人的权限，则从该用户的角色中来
        if (accountAuthorityList.isEmpty()) {
            val accountAuthorityNewList = mutableListOf<AccountAuthority>()
            val account = accountRepository.findOne(accountId) ?: return mutableListOf()
            val roleAuthorityList = roleAuthorityRepository.findByRoleId(account.roleId!!)
            roleAuthorityList.forEach { roleAuthority ->
                val accountAuthority = AccountAuthority().also {
                    it.accountId = accountId
                    it.operationId = roleAuthority.operationId
                    it.operation = roleAuthority.operation
                }
                accountAuthorityNewList.add(accountAuthority)
            }
            return accountAuthorityNewList
        } else {
            return accountAuthorityList
        }*/
    }
}