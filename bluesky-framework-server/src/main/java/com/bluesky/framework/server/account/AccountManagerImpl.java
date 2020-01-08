package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.domain.model.account.AccountRepository;
import com.bluesky.framework.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DubboService
public class AccountManagerImpl implements AccountManager {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public Long insert(Account account) {
        return accountService.insert(account);
    }

    @Override
    public Account findOne(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Account findOneByMobile(String mobile) {
        return accountRepository.findOneByMobile(mobile);
    }

    @Override
    public List<Long> findIdsByRoleId(Long roleId) {
        return accountRepository.findIdsByRoleId(roleId);
    }

    @Override
    public List<Account> findByMobile(String mobile, Integer... roleType) {
        return accountRepository.findByMobile(mobile, roleType);
    }

    @Override
    public Account loginUserName(String userName, String password, List<Integer> roleType, String ip) throws Exception {
        return accountService.loginUserName(userName, password, roleType, ip);
    }

    @Override
    public void update(Account account) {
        accountService.update(account);
    }

    @Override
    public void enable(List<Long> ids) {
        accountService.enable(ids);
    }

    @Override
    public void disable(List<Long> ids) {
        accountService.disable(ids);
    }

    @Override
    public void delete(List<Long> ids) {
        accountService.delete(ids);
    }

    @Override
    public void updatePassword(Long id, String password) {
        accountService.updatePassword(id, password);
    }

    @Override
    public Page<Account> findPageByCondition(String name, String mobile, Long organizationId, Long roleId, Integer status, Integer pageNum, Integer pageSize) {
        return accountRepository.findPageByCondition(name, mobile, organizationId, roleId, status, pageNum, pageSize);
    }

    @Override
    public void restPasswrd(String mobile) {
        accountService.restPasswrd(mobile);
    }

    @Override
    public Account findOneByMobile2(String phone) {
        return accountRepository.findOneByMobile2(phone);
    }
}