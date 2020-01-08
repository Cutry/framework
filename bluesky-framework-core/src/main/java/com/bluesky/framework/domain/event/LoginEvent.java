package com.bluesky.framework.domain.event;

import com.bluesky.framework.account.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mvnsearch.ddd.domain.annotations.DomainEvent;
import org.mvnsearch.ddd.domain.events.BaseDomainEvent;

/**
 * 登录事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@DomainEvent
public class LoginEvent extends BaseDomainEvent<Account> {
    /**
     * accountId
     */
    private Long accountId;
    /**
     * ip
     */
    private String ip;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 说明
     */
    private String memo;
}
