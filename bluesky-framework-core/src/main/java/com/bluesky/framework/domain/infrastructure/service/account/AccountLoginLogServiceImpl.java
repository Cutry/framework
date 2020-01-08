package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.domain.model.account.AccountLoginLogRepository;
import com.bluesky.framework.domain.service.account.AccountLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountLoginLogServiceImpl implements AccountLoginLogService {
    @Autowired
    private AccountLoginLogRepository accountLoginLogRepository;


    @Override
    public long insert(AccountLoginLog accountLoginLog) {
        return accountLoginLogRepository.insert(accountLoginLog);
    }
}