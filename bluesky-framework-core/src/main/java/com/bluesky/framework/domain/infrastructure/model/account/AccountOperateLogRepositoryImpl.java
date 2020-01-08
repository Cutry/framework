package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.AccountOperateLogMapper;
import com.bluesky.framework.domain.model.account.AccountOperateLogRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class AccountOperateLogRepositoryImpl implements AccountOperateLogRepository {
    @Autowired
    private AccountOperateLogMapper accountOperateLogMapper;


    @Override
    public long insert(AccountOperateLog accountOperateLog) {
        accountOperateLogMapper.insert(accountOperateLog);
        return accountOperateLog.getId();
    }


    @Override
    public Page<AccountOperateLog> findByCondition(String accountName, Date timeStart, Date timeEnd, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);
        List<AccountOperateLog> list = accountOperateLogMapper.findByCondition(accountName, timeStart, timeEnd);
        return PageBeanUtils.copyPageProperties(list);
    }
}