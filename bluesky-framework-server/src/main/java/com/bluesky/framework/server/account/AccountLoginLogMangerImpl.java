package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.account.log.AccountLoginLogManager;
import com.bluesky.framework.domain.model.account.AccountLoginLogRepository;
import com.bluesky.framework.domain.service.account.AccountLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@DubboService
public class AccountLoginLogMangerImpl implements AccountLoginLogManager {
    @Autowired
    private AccountLoginLogService accountLoginLogService;
    @Autowired
    private AccountLoginLogRepository accountLoginLogRepository;

    @Override
    public Long insert(AccountLoginLog accountLoginLog) {
        return accountLoginLogService.insert(accountLoginLog);
    }

    @Override
    public Page<AccountLoginLog> findByCondition(String accountName, Integer type, Integer success, Integer pageNum, Integer pageSize) {
        return accountLoginLogRepository.findByCondition(accountName, type, success, pageNum, pageSize);
    }

}