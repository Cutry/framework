package com.bluesky.framework.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mvnsearch.ddd.domain.annotations.DomainEvent;
import org.mvnsearch.ddd.domain.events.BaseDomainEvent;

/**
 * 区域代码修改事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@DomainEvent
public class RegionCodeUpdateEvent extends BaseDomainEvent {
    /**
     * id
     */
    private Long regionId;
    /**
     * 新code
     */
    private String newCode;
}
