package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.log.AccountOperateLog;

public interface AccountOperateLogService {

    long insert(AccountOperateLog accountOperateLog);

}