package com.bluesky.framework.domain.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.log.AccountLoginLog;

public interface AccountLoginLogRepository {

    long insert(AccountLoginLog accountLoginLog);


    Page<AccountLoginLog> findByCondition(String accountName, Integer type, Integer success, Integer pageNum, Integer pageSize);

}