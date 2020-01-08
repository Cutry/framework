package com.bluesky.framework.account.log;

import com.bluesky.common.vo.Page;

import java.util.Date;

public interface AccountOperateLogManager {

    Long insert(AccountOperateLog accountOperateLog);


    Page<AccountOperateLog> findByCondition(String accountName, Date timeStart, Date timeEnd, Integer pageNum, Integer pageSize);

}