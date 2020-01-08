package com.bluesky.framework.domain.event;

import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.constant.AccountLoginLogType;
import com.bluesky.framework.account.constant.AccountLoginResult;
import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.domain.model.account.AccountLoginLogRepository;
import com.bluesky.framework.domain.model.account.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class AccountEventProcessor {
    @Autowired
    private AccountLoginLogRepository accountLoginLogRepository;
    @Autowired
    private OrganizationRepository organizationRepository;


    /**
     * 账户登录事件
     */
    @Async
    @EventListener
    public void processorLoginEvent(LoginEvent loginEvent) {
        Account account = (Account) loginEvent.getPayload();
        AccountLoginLog loginLog = new AccountLoginLog();
        loginLog.setAccountId(account.getId());
        loginLog.setAccountName(account.getName());
        // 单位
        Long organizationId = account.getOrganizationId();
        loginLog.setOrganizationId(organizationId);
        if (organizationId != null) {
            Organization organization = organizationRepository.findOne(organizationId);
            loginLog.setOrganizationName(organization.getName());
        }
        loginLog.setMobile(account.getMobile());
        loginLog.setIp(loginEvent.getIp());
        loginLog.setType(AccountLoginLogType.LOGIN.getCode());
        loginLog.setSuccess(loginEvent.getSuccess() ? AccountLoginResult.SUCCESS.getCode() : AccountLoginResult.FAIL.getCode());
        loginLog.setMemo(loginEvent.getMemo());
        accountLoginLogRepository.insert(loginLog);
    }
}