package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.AccountLoginLogMapper;
import com.bluesky.framework.domain.model.account.AccountLoginLogRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountLoginLogRepositoryImpl implements AccountLoginLogRepository {
    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;


    @Override
    public long insert(AccountLoginLog accountLoginLog) {
        accountLoginLogMapper.insert(accountLoginLog);
        return accountLoginLog.getId();
    }


    @Override
    public Page<AccountLoginLog> findByCondition(String accountName, Integer type, Integer success, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);
        List<AccountLoginLog> list = accountLoginLogMapper.findByCondition(accountName, type, success);
        return PageBeanUtils.copyPageProperties(list);
    }
}