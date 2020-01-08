package com.bluesky.framework.account.log;

import com.bluesky.common.vo.Page;

public interface AccountLoginLogManager {

    Long insert(AccountLoginLog accountLoginLog);


    Page<AccountLoginLog> findByCondition(String accountName, Integer type, Integer success, Integer pageNum, Integer pageSize);

}