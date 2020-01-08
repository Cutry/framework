package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.AccountMapper;
import com.bluesky.framework.domain.model.account.AccountRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Repository
public class AccountRepositoryImpl implements AccountRepository {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public long insert(Account account) {
        accountMapper.insert(account);
        return account.getId();
    }


    @Override
    public Account findOne(long id) {
        return accountMapper.findOne(id);
    }

    @Override
    public Account findOneByMobile(String mobile) {
        return accountMapper.findOneByMobile(mobile);
    }


    @Override
    public List<Account> findByMobile(String mobile, Integer... roleType) {
        List<Integer> list = new ArrayList<>();
        list.addAll(Arrays.asList(roleType));
        return accountMapper.findByMobile(mobile, list);
    }


    @Override
    public List<Long> findIdsByRoleId(long roleId) {
        return accountMapper.findIdsByRoleId(roleId);
    }


    @Override
    public void batchUpdateStatusByMobile(List<Long> mobiles, int status) {
        if (mobiles.isEmpty()) return;
        accountMapper.batchUpdateStatusByMobile(mobiles, status);
    }


    @Override
    public void update(Account account) {
        accountMapper.update(account);
    }


    @Override
    public void batchUpdateStatus(List<Long> ids, int status) {
        if (ids.isEmpty()) return;
        accountMapper.batchUpdateStatus(ids, status);
    }


    @Override
    public Page<Account> findPageByCondition(String name, String mobile, Long organizationId, Long roleId, Integer status, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 15;
        PageHelper.startPage(pageNum, pageSize);
        List<Account> list = accountMapper.findByCondition(name, mobile, organizationId, roleId, status);
        return PageBeanUtils.copyPageProperties(list);
    }

    @Override
    public void UpdateStatusByCompanyId(Long companyId, int status) {
        accountMapper.UpdateStatusByCompanyId(companyId, status);
    }

    @Override
    public void initPasswordByCompanyID(Long companyId) {
        accountMapper.initPasswordByCompanyId(companyId, passwordEncoder.encode("1q2w3e$R"));
    }



    @Override
    public void restPasswrd(String mobile, String password) {
        accountMapper.restPasswrd(mobile,password);
    }

    @Override
    public void updateStatusByMobile(String mobile, int status) {
        accountMapper.updateStatusByMobile(mobile,status);
    }

    @Override
    public Account findOneByMobile2(String phone) {
        return accountMapper.findOneByMobile2(phone);
    }
}