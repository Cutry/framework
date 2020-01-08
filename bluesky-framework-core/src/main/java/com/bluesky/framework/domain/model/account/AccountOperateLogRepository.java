package com.bluesky.framework.domain.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.log.AccountOperateLog;

import java.util.Date;

public interface AccountOperateLogRepository {

    long insert(AccountOperateLog accountOperateLog);


    Page<AccountOperateLog> findByCondition(String accountName, Date timeStart, Date timeEnd, Integer pageNum, Integer pageSize);

}