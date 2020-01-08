package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.log.AccountLoginLog;

public interface AccountLoginLogService {
    long insert(AccountLoginLog accountLoginLog);
}