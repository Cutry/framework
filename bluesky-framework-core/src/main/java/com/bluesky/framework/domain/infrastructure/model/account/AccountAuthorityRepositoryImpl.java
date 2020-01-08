package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.AccountAuthorityMapper;
import com.bluesky.framework.domain.model.account.AccountAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AccountAuthorityRepositoryImpl implements AccountAuthorityRepository {
    @Autowired
    private AccountAuthorityMapper accountAuthorityMapper;

    @Override
    public void saveAuthorities(List<AccountAuthority> accountAuthorityList) {
        accountAuthorityMapper.saveAuthorities(accountAuthorityList);
    }

    @Override
    public List<AccountAuthority> findByAccountId(long accountId) {
        return accountAuthorityMapper.findByAccountId(accountId);
    }

    @Override
    public void deleteByAccountId(long accountId) {
        accountAuthorityMapper.deleteByAccountId(accountId);
    }

    @Override
    public void deleteByAccountIdsAndOperationId(List<Long> accountIds, long operationId) {
        accountAuthorityMapper.deleteByAccountIdsAndOperationId(accountIds, operationId);
    }
}