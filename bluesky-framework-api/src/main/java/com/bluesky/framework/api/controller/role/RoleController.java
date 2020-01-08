package com.bluesky.framework.api.controller.role;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.constant.AdministrativeLevel;
import com.bluesky.framework.account.constant.OperateLogStep;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.api.controller.common.IPUtils;
import com.bluesky.framework.api.event.LogEventProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 角色管理
 * 角色只有系统管理员才有权限
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private LogEventProcessor logEventProcessor;


    @PostMapping("/add")
    @PreAuthorize("hasPermission('crm-api','om_role_manage')")
    public DataResponse add(@AuthenticationPrincipal Account account, HttpServletRequest request, @RequestParam String name, @RequestParam Integer level) {
        DataResponse result = new DataResponse();

        if (!isNameLegal(name, result)) {
            return result;
        }
        if (!isLevelLegal(level, result)) {
            return result;
        }
        Role role = Role.builder().type(RoleType.GOVERNMENT.getCode()).level(level).name(name).build();
        Long id = roleManager.insert(role);
        result.addData("id", id);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRole(account, OperateLogStep.ROLE_ADD.getCode(), ip);
        return result;
    }


    @PutMapping("/{id}/delete")
    @PreAuthorize("hasPermission('crm-api','om_role_manage')")
    public DataResponse delete(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id) {
        DataResponse result = new DataResponse();
        if (isIdExist(id, result) == null) {
            return result;
        }

        // 检查角色下是否挂有用户。有用户的话无法删除
        List<Long> accountIds = accountManager.findIdsByRoleId(id);
        if (!accountIds.isEmpty()) {
            return result.setFalseAndMessage("有账号绑定该角色，请先删除该角色下的账户");
        }
        roleManager.delete(id);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRole(account, OperateLogStep.ROLE_DELETE.getCode(), ip);
        return result;
    }


    @PutMapping("/{id}/update_name")
    @PreAuthorize("hasPermission('crm-api','om_role_manage')")
    public DataResponse updateName(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id, @RequestParam String name) {
        DataResponse result = new DataResponse();
        if (!isNameLegal(name, result)) {
            return result;
        }
        // id是否存在
        if (isIdExist(id, result) == null) {
            return result;
        }
        Role role = Role.builder().id(id).name(name).build();
        roleManager.update(role);
        role = roleManager.findOne(id);
        result.addData("role", role);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRole(account, OperateLogStep.ROLE_UPDATE.getCode(), ip);
        return result;
    }


    @GetMapping("/all")
    public DataResponse getAllLowerLevelRoles(@AuthenticationPrincipal Account account) {
        DataResponse result = new DataResponse();
        Role role = roleManager.findOne(account.getRoleId());
        List<Role> list = roleManager.findGovernmentLowerLevelRoles(role.getLevel());
        // 是否初始化
        boolean isRegion = regionManager.isInit();
        List<Role> filterList;
        if (isRegion) {
            // 查询系统等级，只返回比系统等级level大的角色
            int systemLevel = regionManager.findSystemLevel();
            filterList = list.stream().filter(roleTmp -> roleTmp.getLevel() >= systemLevel).collect(Collectors.toList());
        } else {
            filterList = list;
        }
        Map<Integer, List<Role>> map = filterList.stream().collect(Collectors.groupingBy(Role::getLevel));
        result.addData("result", map);

        // 查询系统级别
        int systemLevel = regionManager.findSystemLevel();
        result.addData("systemLevel", systemLevel);
        return result;
    }


    /**
     * 检查id是否能查询到角色
     */
    private Role isIdExist(Long id, DataResponse result) {
        Role role = roleManager.findOne(id);
        if (role == null) {
            result.setFalseAndMessage("角色不存在");
            return null;
        }
        return role;
    }

    /**
     * 角色名称是否合法
     *
     * @param name   角色名称
     * @param result 结果
     */
    private Boolean isNameLegal(String name, DataResponse result) {
        if (StringUtils.isBlank(name) || name.length() < 30) {
            return true;
        }
        result.setFalseAndMessage("角色名称超出长度");
        return false;
    }


    /**
     * 校验行政级别是否合法
     *
     * @param level  区域级别
     * @param result 返回结果
     * @return true合法，false不合法
     */
    private Boolean isLevelLegal(Integer level, DataResponse result) {
        if (AdministrativeLevel.isCodeLegal(level)) {
            return true;
        }
        result.setFalseAndMessage("行政级别选择错误");
        return false;
    }
}