package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.domain.event.RegionCodeUpdateEvent;
import com.bluesky.framework.domain.model.account.RegionRepository;
import com.bluesky.framework.domain.service.account.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public long insert(Region region) {
        return regionRepository.insert(region);
    }


    @Override
    public void update(Region region) {
        Region old = regionRepository.findOne(region.getId());
        regionRepository.update(region);
        if (!Objects.equals(old.getCode(), region.getCode())) {
            RegionCodeUpdateEvent regionCodeUpdateEvent = new RegionCodeUpdateEvent();
            regionCodeUpdateEvent.setRegionId(region.getId());
            regionCodeUpdateEvent.setNewCode(region.getCode());
            applicationEventPublisher.publishEvent(regionCodeUpdateEvent);
        }
    }


    @Override
    public void updateSystemLevel(int level) {
        regionRepository.updateSystemLevel(level);
    }


    @Override
    public void delete(long id) {
        regionRepository.delete(id);
    }
}