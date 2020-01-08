package com.bluesky.framework.api.controller.auth;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.account.auth.RoleAuthorityManager;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.menu.MenuSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 角色权限配置管理
 */
@RestController
@RequestMapping("/role/auth")
public class RoleAuthorityController {
    @Autowired
    private RoleAuthorityManager roleAuthorityManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private MenuSourceManager menuSourceManager;

    /**
     * 根据省级等级别查询角色及角色权限信息
     */
    @GetMapping("/level")
    public DataResponse findRoleByLevel(Integer level) {
        DataResponse result = new DataResponse();

        List<Role> roleList = roleManager.findGovernmentRolesByLevel(level);
        List<Long> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());
        Map<Long, List<MenuSource>> roleAuthorityDetailMap = new HashMap<>();
        //每个角色
        if (!roleIdList.isEmpty()) {
            List<RoleAuthority> roleAuthorityList = roleAuthorityManager.findByRoleIds(roleIdList);
            Map<Long, List<RoleAuthority>> roleAuthorityMap = roleAuthorityList.stream().collect(Collectors.groupingBy(RoleAuthority::getRoleId));
            roleAuthorityMap.forEach((roleId, roleAuthorities) -> {
                List<Long> operationIdList = roleAuthorities.stream().map(RoleAuthority::getOperationId).collect(Collectors.toList());
                if (operationIdList.isEmpty()) {
                    return;
                }
                //每个角色的操作权限
                List<MenuSource> menuSourceList = menuSourceManager.findByOperationIds(operationIdList);
                roleAuthorityDetailMap.put(roleId, menuSourceList);
            });
        }

        //角色列表
        result.addData("roleList", roleList);
        result.addData("roleAuthorityDetailMap", roleAuthorityDetailMap);
        return result;
    }

    /**
     * 某个角色的权限信息
     */
    @GetMapping("/{roleId}/detail")
    public DataResponse findByRoleId(@PathVariable Long roleId) {
        DataResponse result = new DataResponse();
        //当前角色
        List<RoleAuthority> roleAuthorityList = roleAuthorityManager.findByRoleId(roleId);
        List<Long> operationIdList = roleAuthorityList.stream().map(RoleAuthority::getOperationId).collect(Collectors.toList());
        //角色的操作权限
        // roleMenuSourceList = menuSourceManager.findByOperationIds(operationIdList)
        //result.addData("roleMenuSourceList", roleMenuSourceList)
        //已经有的权限，只给操作项ID足够，根据id去默认勾选
        result.addData("operationIdList", operationIdList);

        //全部菜单及操作权限
        List<MenuSource> menuSourceList = menuSourceManager.findAllMenuAndOperation();
        result.addData("menuSourceList", menuSourceList);
        return result;
    }

    /**
     * 保存角色权限信息
     */
    @PreAuthorize("hasPermission('crm-api', 'om_auth_manage')")
    @PostMapping("{roleId}/save")
    public DataResponse updateMenu(@PathVariable Long roleId,
                                   @RequestBody List<RoleAuthority> roleAuthorityList) {
        DataResponse result = new DataResponse();
        roleAuthorityManager.saveAuthorities(roleId, roleAuthorityList);
        return result;
    }
}