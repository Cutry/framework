package com.bluesky.framework.domain.event;

import com.bluesky.framework.account.auth.RoleAuthority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mvnsearch.ddd.domain.annotations.DomainEvent;
import org.mvnsearch.ddd.domain.events.BaseDomainEvent;

import java.util.List;

/**
 * 区域代码修改事件
 */
@Data
@DomainEvent
@EqualsAndHashCode(callSuper = true)
public class RoleAuthorityDeleteEvent extends BaseDomainEvent {
    /**
     * id
     */
    private Long roleId;
    /**
     * 被删除的操作ids
     */
    private List<Long> operationDeleteIds;
    /**
     * 新增的操作
     */
    private List<RoleAuthority> operationAddList;
}
