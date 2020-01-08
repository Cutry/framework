package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.constant.AccountStatus;
import com.bluesky.framework.domain.event.AccountAddEvent;
import com.bluesky.framework.domain.event.LoginEvent;
import com.bluesky.framework.domain.model.account.AccountRepository;
import com.bluesky.framework.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public long insert(Account account) {
        // 原始密码加密
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        long accountId = accountRepository.insert(account);
        publishAccountAddEvent(account);
        return accountId;
    }


    @Override
    public void update(Account account) {
        accountRepository.update(account);
    }


    @Override
    public void enable(List<Long> ids) {
        accountRepository.batchUpdateStatus(ids, AccountStatus.NORMAl.getCode());
    }


    @Override
    public void disable(List<Long> ids) {
        accountRepository.batchUpdateStatus(ids, AccountStatus.DISABLE.getCode());
    }


    @Override
    public void delete(List<Long> ids) {
        accountRepository.batchUpdateStatus(ids, AccountStatus.DELETE.getCode());
    }


    @Override
    public void updatePassword(long id, String password) {
        password = passwordEncoder.encode(password);
        Account account = Account.builder().id(id).password(password).build();
        accountRepository.update(account);
    }


    @Override
    public Account loginUserName(String userName, String password, List<Integer> roleType, String ip) throws Exception {
        List<Account> accountList = accountRepository.findByMobile(userName, roleType.toArray(new Integer[0]));
        // 账户不存在
        if (accountList.isEmpty()) {
            throw new Exception("Z-100101");
        }
        Account account = accountList.get(0);
        // 登录事件
        LoginEvent loggingEvent = new LoginEvent();
        loggingEvent.setAccountId(account.getId());
        loggingEvent.setIp(ip);
        loggingEvent.setPayload(account);
        loggingEvent.setSuccess(false);


        // 密码不正确
        if (!passwordEncoder.matches(password, account.getPassword())) {
            loggingEvent.setMemo("登录失败，用户名或密码错误");
            throw new Exception("Z-100102");
        }

        // 账户状态异常
        if (Objects.equals(account.getStatus(), AccountStatus.DISABLE.getCode())) {
            loggingEvent.setMemo("登录失败，账户已禁用");
            throw new Exception("Z-100103");
        } else if (Objects.equals(account.getStatus(), AccountStatus.LOCKED.getCode())) {
            loggingEvent.setMemo("登录失败，此账户已锁定");
            throw new Exception("Z-100104");
        } else if (Objects.equals(account.getStatus(), AccountStatus.DELETE.getCode())) {
            loggingEvent.setMemo("登录失败，此账户不存在");
            throw new Exception("Z-100105");
        }


        loggingEvent.setSuccess(true);
        loggingEvent.setMemo("登录成功");
        applicationEventPublisher.publishEvent(loggingEvent);
        return account;
    }

    @Override
    public void restPasswrd(String mobile) {
        String password = passwordEncoder.encode("1q2w3e$R");
        accountRepository.restPasswrd(mobile,password);
    }

    /**
     * 发布用户新增事件
     */
    private void publishAccountAddEvent(Account account) {
        AccountAddEvent accountAddEvent = new AccountAddEvent();
        accountAddEvent.setPayload(account);
        applicationEventPublisher.publishEvent(accountAddEvent);
    }
}