package com.bluesky.framework.api.controller.config;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.account.auth.AccountAuthorityManager;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.PageSettingManager;
import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import com.bluesky.framework.setting.page.PageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 系统配置管理
 */
@RestController
@RequestMapping("/config")
class SystemConfigController {
    @Autowired
    private PageSettingManager pageSettingManager;
    @Autowired
    private MenuSourceManager menuSourceManager;
    @Autowired
    private AccountAuthorityManager accountAuthorityManager;

    @Value("${bluesky.version}")
    private String blueskyVersion;

    @GetMapping("/system/info")
    public DataResponse systemInfo() {
        DataResponse result = new DataResponse();
        result.addData("blueskyVersion", blueskyVersion);
        return result;
    }

    @GetMapping("/page")
    public DataResponse findAll() {
        DataResponse result = new DataResponse();
        List<PageSetting> pageSettingList = pageSettingManager.findAll();
        //根据不同的页面分组
        Map<String, List<PageSetting>> pageSettingMap = pageSettingList.stream().collect(Collectors.groupingBy(PageSetting::getPage));
        result.addData("pageSettingMap", pageSettingMap);
        return result;
    }

    @GetMapping("/menu")
    public DataResponse findAllMenu(@AuthenticationPrincipal Account account) {
        DataResponse result = new DataResponse();
        if (account == null) {
            result.addData("menuSourceList", new ArrayList<>());
            return result;
        } else {
            //系统管理员
            if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
                List<MenuSource> menuSourceList = menuSourceManager.findAllMenuAndOperation();
                return result.addData("menuSourceList", menuSourceList);
            } else if (account.getRoleType() == RoleType.GOVERNMENT.getCode()) {
                //根据当前用户进行权限过滤
                List<AccountAuthority> accountAuthorityList = accountAuthorityManager.findByAccountId(account.getId());
                //所属角色的菜单及操作权限
                List<Long> operationIdList = accountAuthorityList.stream().map(AccountAuthority::getOperationId).collect(Collectors.toList());
                List<MenuSource> menuSourceList = menuSourceManager.findByOperationIds(operationIdList);
                return result.addData("menuSourceList", menuSourceList);
            } else {
                return result.addData("menuSourceList", new ArrayList<>());
            }
        }
    }

    /**
     * 获取菜单
     */
    @GetMapping("/menu/{id}")
    public DataResponse findMenuById(@AuthenticationPrincipal Account account, @PathVariable Long id) {
        DataResponse result = new DataResponse();
        if (account == null) {
            result.setSuccess("UNLOGIN");
            result.setCode("401");
            result.setMessage("您未登录或登录超时!");
            return result;
        } else {
            MenuSource menuSource = menuSourceManager.findOne(id);
            if (menuSource == null) {
                return result.setFalseAndMessage("菜单不存在,请刷新后重试!");
            }
            //系统管理员
            if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
                return result.addData("menuSource", menuSource);
            } else if (account.getRoleType() == RoleType.GOVERNMENT.getCode()) {
                //根据当前用户进行权限过滤
                List<AccountAuthority> accountAuthorityList = accountAuthorityManager.findByAccountId(account.getId());
                List<Long> operationIdList = accountAuthorityList.stream().map(AccountAuthority::getOperationId).collect(Collectors.toList());
                //菜单拥有的操作项
                List<MenuSourceOperation> menuSourceOperation = menuSourceManager.findOperationByMenuId(menuSource.getId());
                Optional<MenuSourceOperation> findOperation = menuSourceOperation.stream().filter(menuSourceOperationTemp -> operationIdList.contains(menuSourceOperationTemp.getId())).findFirst();
                if (!findOperation.isPresent()) {
                    result.setSuccess("FORBIDDEN");
                    result.setCode("403");
                    result.setMessage("您没有权限操作!");
                    return result;
                } else {
                    return result.addData("menuSource", menuSource);
                }
            } else {
                result.setSuccess("FORBIDDEN");
                result.setCode("403");
                result.setMessage("您没有权限操作!");
                return result;
            }
        }
    }
}