package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.domain.model.account.AccountOperateLogRepository;
import com.bluesky.framework.domain.service.account.AccountOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
class AccountOperateLogServiceImpl implements AccountOperateLogService {
    @Autowired
    private AccountOperateLogRepository accountOperateLogRepository;

    @Override
    public long insert(AccountOperateLog accountOperateLog) {
        return accountOperateLogRepository.insert(accountOperateLog);
    }
}