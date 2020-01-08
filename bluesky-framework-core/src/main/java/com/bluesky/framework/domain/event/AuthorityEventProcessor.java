package com.bluesky.framework.domain.event;

import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.domain.model.account.AccountAuthorityRepository;
import com.bluesky.framework.domain.model.account.AccountRepository;
import com.bluesky.framework.domain.model.account.RoleAuthorityRepository;
import org.apache.tools.ant.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class AuthorityEventProcessor {
    @Autowired
    private AccountAuthorityRepository accountAuthorityRepository;
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(AuthorityEventProcessor.class);

    @Async
    @EventListener
    public void processRoleAuthorityDeleteEvent(RoleAuthorityDeleteEvent roleAuthorityDeleteEvent) {
        Long roleId = roleAuthorityDeleteEvent.getRoleId();
        if (roleId == null) return;
        List<Long> operationDeleteIds = roleAuthorityDeleteEvent.getOperationDeleteIds();
        List<RoleAuthority> operationAddList = roleAuthorityDeleteEvent.getOperationAddList();
        logger.info("角色ID roleId = $roleId");

        List<Long> accountIdList = accountRepository.findIdsByRoleId(roleId);
        if (!accountIdList.isEmpty() && operationDeleteIds != null && !operationDeleteIds.isEmpty()) {
            for (Long it : operationDeleteIds) {
                // 删除
                logger.info("删除的操作  operationIds = {}", StringUtils.join(operationDeleteIds, "，"));
                accountAuthorityRepository.deleteByAccountIdsAndOperationId(accountIdList, it);
            }
        }
        if (!accountIdList.isEmpty() && operationAddList != null && !operationAddList.isEmpty()) {
            logger.info("新增的操作  operationIds = {}", StringUtils.join(operationAddList.stream().map(RoleAuthority::getOperationId).collect(Collectors.toList()), "，"));
            // 新增
            List<AccountAuthority> accountAuthorityList = new ArrayList<>();
            for (Long accountId : accountIdList) {
                for (RoleAuthority roleAuthority : operationAddList) {
                    AccountAuthority accountAuthority = AccountAuthority.builder().accountId(accountId).operationId(roleAuthority.getOperationId()).operation(roleAuthority.getOperation()).build();
                    accountAuthorityList.add(accountAuthority);
                }
            }
            accountAuthorityRepository.saveAuthorities(accountAuthorityList);
        }
    }


    @Async
    @EventListener
    public void processAccountAddForAuthEvent(AccountAddEvent accountAddEvent) {
        Account account = accountAddEvent.getPayload();
        //单位的用户新增，默认与角色一致的权限
        if (Objects.equals(account.getRoleType(), RoleType.GOVERNMENT.getCode())) {
            List<RoleAuthority> roleAuthorityList = roleAuthorityRepository.findByRoleId(account.getRoleId());
            if (roleAuthorityList.isEmpty()) {
                return;
            }
            List<AccountAuthority> accountAuthorityList = new ArrayList<>();
            for (RoleAuthority roleAuthority : roleAuthorityList) {
                AccountAuthority accountAuthority = AccountAuthority.builder().accountId(account.getId()).operationId(roleAuthority.getOperationId()).operation(roleAuthority.getOperation()).build();
                accountAuthorityList.add(accountAuthority);
            }
            accountAuthorityRepository.saveAuthorities(accountAuthorityList);

        }
    }
}