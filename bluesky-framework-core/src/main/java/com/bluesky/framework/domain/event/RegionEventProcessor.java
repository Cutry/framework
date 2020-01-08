package com.bluesky.framework.domain.event;

import com.bluesky.framework.domain.model.account.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
class RegionEventProcessor {
    @Autowired
    private RegionRepository regionRepository;

    private Logger logger = LoggerFactory.getLogger(RegionEventProcessor.class);

    @Async
    @EventListener
    public void processRegionCodeUpdateEvent(RegionCodeUpdateEvent regionCodeUpdateEvent) {
        Long id = regionCodeUpdateEvent.getRegionId();
        String newCode = regionCodeUpdateEvent.getNewCode();
        logger.warn("区域code修改：id = $id,code = $newCode");
        // 更新所有子级中记录的parentCode
        regionRepository.updateParentCodeByParentId(id, newCode);
    }
}