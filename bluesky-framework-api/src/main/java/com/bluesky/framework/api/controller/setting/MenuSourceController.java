package com.bluesky.framework.api.controller.setting;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统配置管理
 */
@RestController
@RequestMapping("/setting/menu")
public class MenuSourceController {
    @Autowired
    private MenuSourceManager menuSourceManager;

    /**
     * 菜单
     */
    @GetMapping("/find")
    public DataResponse find(@RequestParam Long parentId) {
        DataResponse result = new DataResponse();
        List<MenuSource> menuSourceList = menuSourceManager.findByParentId(parentId);
        result.addData("menuSourceList", menuSourceList);
        return result;
    }

    /**
     * 菜单操作项
     */
    @GetMapping("/operation/find")
    public DataResponse findOperation(@RequestParam Long menuId) {
        DataResponse result = new DataResponse();
        List<MenuSourceOperation> operationList = menuSourceManager.findOperationByMenuId(menuId);
        result.addData("operationList", operationList);
        return result;
    }

    /**
     * 菜单新增
     *
     * @param menuSource 菜单
     *                   code 菜单编码
     *                   name 菜单名称
     *                   parentId 父ID
     *                   url 链接
     *                   seq 顺序
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("save")
    public DataResponse save(@RequestBody MenuSource menuSource) {
        DataResponse result = new DataResponse();
        //校验数据
        if (!isMenuSourceLegal(menuSource, result)) {
            return result;
        }
        //code唯一性判断
        if (menuSourceManager.isMenuCodeUnique(menuSource.getCode())) {
            //非系统菜单
            menuSource.setSystemMenu(false);
            menuSourceManager.saveMenuSource(menuSource);
        } else {
            result.setFalseAndMessage("菜单编码已存在,请重新设置");
        }
        return result;
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("{id}/delete")
    public DataResponse delete(@PathVariable Long id) {
        DataResponse result = new DataResponse();
        MenuSource menuSource = menuSourceManager.findOne(id);
        if (menuSource == null) {
            return result.setFalseAndMessage("菜单不存在,请刷新后重试");
        }
        if (menuSource.getSystemMenu() != null && menuSource.getSystemMenu()) {
            return result.setFalseAndMessage("不能删除系统菜单");
        } else {
            menuSourceManager.deleteMenuSource(id);
            return result;
        }
    }

    /**
     * 菜单更新名称
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("{id}/update")
    public DataResponse updateMenu(@PathVariable Long id, String name, String url) {
        DataResponse result = new DataResponse();
        //数据合法性校验
        MenuSource menuSource = menuSourceManager.findOne(id);
        if (menuSource == null) {
            return result.setFalseAndMessage("操作项所属菜单不存在,请刷新后重试");
        }
        //顶级菜单
        if (menuSource.getParentId() == 0L) {
            menuSourceManager.updateMenuSourceName(id, name);
        } else {
            menuSourceManager.updateMenuSourceNameUrl(id, name, url);
        }
        return result;
    }

    /**
     * 菜单排序
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("/sort")
    public DataResponse sortMenu(@RequestParam String ids) {
        DataResponse result = new DataResponse();
        //ids集合转换
        Map<Long, Integer> data = new LinkedHashMap<>();
        String[] idArray = ids.split(",");
        Integer index = 0;
        for (String idStr : idArray) {
            data.put(Long.valueOf(idStr), index);
            index++;
        }
        menuSourceManager.sortMenuSource(data);
        return result;
    }

    /**
     * 新增菜单操作项
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("/operation/save")
    public DataResponse operationSave(Long menuId, String operation, String operationName) {
        DataResponse result = new DataResponse();
        //唯一性校验
        if (!menuSourceManager.isOperationUnique(operation)) {
            return result.setFalseAndMessage("操作项编码已存在,请重新设置");
        }
        //数据合法性校验
        if (menuSourceManager.findOne(menuId) == null) {
            return result.setFalseAndMessage("操作项所属菜单不存在,请刷新后重试");
        }

        MenuSourceOperation menuSourceOperation = MenuSourceOperation.builder()
                .menuId(menuId)
                .operation(operation)
                .operationName(operationName).build();

        menuSourceManager.saveMenuSourceOperation(menuSourceOperation);
        return result;
    }

    /**
     * 操作项删除
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("/operation/{id}/delete")
    public DataResponse operationDelete(@PathVariable Long id, Long menuId) {
        DataResponse result = new DataResponse();
        //数据合法性校验
        MenuSource menuSource = menuSourceManager.findOne(menuId);
        if (menuSource == null) {
            return result.setFalseAndMessage("操作项所属菜单不存在,请刷新后重试");
        }
        if (menuSource.getSystemMenu() != null && menuSource.getSystemMenu()) {
            return result.setFalseAndMessage("不能删除系统菜单的操作项");
        } else {
            menuSourceManager.deleteMenuSourceOperation(id);
        }
        return result;
    }

    /**
     * 更新操作项名称
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_menu_manage')")
    @PostMapping("/operation/{id}/update")
    public DataResponse operationUpdate(@PathVariable Long id, String name) {
        DataResponse result = new DataResponse();
        menuSourceManager.updateOperationName(id, name);
        return result;
    }

    /**
     * 应用设置样式
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_index_manage')")
    @PostMapping("/operation/setting")
    public DataResponse update(@RequestBody MenuSource menuSource) {
        DataResponse result = new DataResponse();
        menuSourceManager.updateMenuSourceStyle(menuSource);
        return result;
    }

    /**
     * 新增菜单的校验
     */
    private Boolean isMenuSourceLegal(MenuSource menuSource, DataResponse result) {
        if (StringUtils.isBlank(menuSource.getCode())) {
            result.setFalseAndMessage("菜单编码不能为空");
            return false;
        }
        if (StringUtils.isBlank(menuSource.getName())) {
            result.setFalseAndMessage("菜单名称不能为空");
            return false;
        }
        if (menuSource.getSeq() == null) {
            result.setFalseAndMessage("菜单顺序不能为空");
            return false;
        }
        if (menuSource.getParentId() == null) {
            menuSource.setParentId(0L);
        }
        if (StringUtils.isBlank(menuSource.getUrl())) {
            menuSource.setUrl(null);
        }
        return true;
    }
}