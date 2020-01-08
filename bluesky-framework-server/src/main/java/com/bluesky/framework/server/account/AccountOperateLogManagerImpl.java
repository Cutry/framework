package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.account.log.AccountOperateLogManager;
import com.bluesky.framework.domain.model.account.AccountOperateLogRepository;
import com.bluesky.framework.domain.service.account.AccountOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@DubboService
public class AccountOperateLogManagerImpl implements AccountOperateLogManager {
    @Autowired
    private AccountOperateLogService accountOperateLogService;
    @Autowired
    private AccountOperateLogRepository accountOperateLogRepository;

    @Override
    public Long insert(AccountOperateLog accountOperateLog) {
        return accountOperateLogService.insert(accountOperateLog);
    }

    @Override
    public Page<AccountOperateLog> findByCondition(String accountName, Date timeStart, Date timeEnd, Integer pageNum, Integer pageSize) {
        return accountOperateLogRepository.findByCondition(accountName, timeStart, timeEnd, pageNum, pageSize);
    }
}