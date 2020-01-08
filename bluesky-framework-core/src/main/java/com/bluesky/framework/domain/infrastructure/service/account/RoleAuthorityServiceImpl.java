package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.domain.event.RoleAuthorityDeleteEvent;
import com.bluesky.framework.domain.model.account.RoleAuthorityRepository;
import com.bluesky.framework.domain.service.account.RoleAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
class RoleAuthorityServiceImpl implements RoleAuthorityService {
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Logger logger = LoggerFactory.getLogger(RoleAuthorityServiceImpl.class);

    @Override
    public void saveAuthorities(long roleId, List<RoleAuthority> roleAuthorityList) {
        //原来的权限
        List<RoleAuthority> roleAuthorityListInDB = roleAuthorityRepository.findByRoleId(roleId);
        //发布权限变更事件
        publishRoleDeleteEvent(roleId, roleAuthorityListInDB, roleAuthorityList);
        if (!roleAuthorityListInDB.isEmpty()) {
            //先删除再保存
            roleAuthorityRepository.deleteByRoleId(roleId);
        }
        if (!roleAuthorityList.isEmpty()) {
            roleAuthorityList.forEach(it -> it.setRoleId(roleId));
            roleAuthorityRepository.saveAuthorities(roleAuthorityList);
        }
    }

    /**
     * 角色修改权限事件发布
     * 可能从空设置权限
     * 可能修改了权限
     */
    private void publishRoleDeleteEvent(long roleId, List<RoleAuthority> roleAuthorityListInDB, List<RoleAuthority> roleAuthorityList) {
        List<Long> deleteIdList = new ArrayList<>();
        List<RoleAuthority> addList = new ArrayList<>();
        //新的权限
        Map<Long, RoleAuthority> roleAuthorityMap = roleAuthorityList.stream().collect(Collectors.toMap(RoleAuthority::getOperationId, it -> it));
        Map<Long, RoleAuthority> roleAuthorityInDBMap = roleAuthorityListInDB.stream().collect(Collectors.toMap(RoleAuthority::getOperationId, it -> it));

        //找出删除的权限，告知用户权限
        for (RoleAuthority it : roleAuthorityListInDB) {
            //原来数据库中在新的中找不到则为删除
            if (roleAuthorityMap.get(it.getOperationId()) == null) {
                deleteIdList.add(it.getOperationId());
            }
        }
        //找出新增的权限
        for (RoleAuthority it : roleAuthorityList) {
            if (roleAuthorityInDBMap.get(it.getOperationId()) == null) {
                addList.add(it);
            }
        }
        if (deleteIdList.isEmpty() && addList.isEmpty()) {
            logger.info("roleId  $roleId 没有被删除及新增的权限");
            return;
        }
        //发送消息
        RoleAuthorityDeleteEvent roleAuthorityDeleteEvent = new RoleAuthorityDeleteEvent();
        roleAuthorityDeleteEvent.setRoleId(roleId);
        roleAuthorityDeleteEvent.setOperationDeleteIds(deleteIdList);
        roleAuthorityDeleteEvent.setOperationAddList(addList);

        applicationEventPublisher.publishEvent(roleAuthorityDeleteEvent);
    }
}