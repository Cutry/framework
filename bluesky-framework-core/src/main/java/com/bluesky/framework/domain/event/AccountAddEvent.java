package com.bluesky.framework.domain.event;

import com.bluesky.framework.account.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mvnsearch.ddd.domain.annotations.DomainEvent;
import org.mvnsearch.ddd.domain.events.BaseDomainEvent;

/**
 * 账户新增事件
 */
@Data
@DomainEvent
@EqualsAndHashCode(callSuper = true)
public class AccountAddEvent extends BaseDomainEvent<Account> {

}
