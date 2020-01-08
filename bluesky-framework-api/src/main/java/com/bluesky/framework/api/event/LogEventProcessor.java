package com.bluesky.framework.api.event;

import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.constant.AccountLoginLogType;
import com.bluesky.framework.account.constant.AccountLoginResult;
import com.bluesky.framework.account.constant.OperateLogStep;
import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.account.log.AccountLoginLogManager;
import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.account.log.AccountOperateLogManager;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 操作日志记录
 */
@Component
public class LogEventProcessor {
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private AccountOperateLogManager accountOperateLogManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private AccountLoginLogManager loginLogManager;

    private Logger logger = LoggerFactory.getLogger(LogEventProcessor.class);

    @Async
    public void logOperateRegion(Account currentAccount, Long operateAccountId, String logStep, String ip) {
        Account richAccount = accountManager.findOne(currentAccount.getId());
        // 单位
        Long organizationId = richAccount.getOrganizationId();
        Organization organization = null;
        if (organizationId != null) {
            organization = organizationManager.findOne(organizationId);
        }

        String stepName = OperateLogStep.getName(logStep);

        // 被操作人
        String operateAccountStr = "";
        if (operateAccountId != null) {
            Account operateAccount = accountManager.findOne(operateAccountId);
            operateAccountStr = "被操作人：" + operateAccount.getName();
        }

        String memo = "用户管理，步骤名称：" + stepName + "，步骤编码：" + logStep + "，操作人：" + richAccount.getName() + "，" +
                operateAccountStr + ",操作ip：" + ip + ",操作时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        AccountOperateLog accountOperateLog = AccountOperateLog.builder()
                .accountId(currentAccount.getId())
                .accountName(currentAccount.getName())
                .organizationId(organizationId)
                .organizationName(organization != null ? organization.getName() : "")
                .mobile(currentAccount.getMobile())
                .ip(ip)
                .stepCode(logStep)
                .step(stepName)
                .createdAt(new Date())
                .memo(memo).build();

        accountOperateLogManager.insert(accountOperateLog);
    }

    /**
     * 记录退出登录日志
     */
    @Async
    public void logoutEvent(Long accountId, String ip) {
        Account account = accountManager.findOne(accountId);
        // 单位
        Organization organization = null;
        Long organizationId = account.getOrganizationId();
        if (organizationId != null) {
            organization = organizationManager.findOne(organizationId);
        }
        AccountLoginLog accountLoginLog = AccountLoginLog.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .organizationId(organizationId)
                .organizationName(organization != null ? organization.getName() : null)
                .mobile(account.getMobile())
                .ip(ip)
                .type(AccountLoginLogType.LOGOUT.getCode())
                .success(AccountLoginResult.SUCCESS.getCode())
                .memo("退出登录").build();
        loginLogManager.insert(accountLoginLog);
    }

    @Async
    public void logOperateRegion(Account account, String logStep, String ip) {
        AccountOperateLog accountOperateLog = AccountOperateLog.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                //单位管理只有系统管理员可以，不记录
                .mobile(account.getMobile())
                .ip(ip)
                .stepCode(logStep)
                .step(OperateLogStep.getName(logStep))
                .createdAt(new Date())
                .memo("单位管理，步骤名称：" + OperateLogStep.getName(logStep) + "，步骤编码：" + logStep +
                        "，操作人：" + account.getName() + "，操作ip：" + ip +
                        ",操作时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build();

        accountOperateLogManager.insert(accountOperateLog);
    }

    @Async
    public void logOperateRole(Account account, String logStep, String ip) {
        AccountOperateLog accountOperateLog = AccountOperateLog.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                //单位管理只有系统管理员可以，不记录
                .mobile(account.getMobile())
                .ip(ip)
                .stepCode(logStep)
                .step(OperateLogStep.getName(logStep))
                .createdAt(new Date())
                .memo("角色管理，步骤名称：" + OperateLogStep.getName(logStep) + "，步骤编码：" + logStep +
                        "，操作人：" + account.getName() + "，操作ip：" + ip +
                        ",操作时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build();

        accountOperateLogManager.insert(accountOperateLog);
    }

}
