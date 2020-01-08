package com.bluesky.framework.api.controller.log;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.log.AccountLoginLog;
import com.bluesky.framework.account.log.AccountLoginLogManager;
import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.account.log.AccountOperateLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日志查询
 */
@RestController
@PreAuthorize("hasPermission('crm-api','om_log_view')")
public class LogController {
    @Autowired
    private AccountOperateLogManager operateLogManager;
    @Autowired
    private AccountLoginLogManager loginLogManager;


    /**
     * 查询登录日志
     */
    @GetMapping("/get_login_log")
    public DataResponse findLoginLog(@RequestParam(required = false) String accountName,
                                     @RequestParam(required = false) Integer type,
                                     @RequestParam(required = false) Integer success,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
        DataResponse result = new DataResponse();
        Page<AccountLoginLog> page = loginLogManager.findByCondition(accountName, type, success, pageNum, 20);
        result.addData("page", page);
        return result;
    }


    /**
     * 查询操作日志
     */
    @GetMapping("/get_operate_log")
    public DataResponse findOperateLog(@RequestParam(required = false) String accountName, @RequestParam(required = false) Date timeStart,
                                       @RequestParam(required = false) Date timeEnd, @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
        DataResponse result = new DataResponse();
        Page<AccountOperateLog> page = operateLogManager.findByCondition(accountName, timeStart, timeEnd, pageNum, 20);
        result.addData("page", page);
        return result;
    }


    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     *
     * @param binder 数据格式
     */
    @InitBinder
    private void initBinder(ServletRequestDataBinder binder) {
        //1、日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);
        //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换
        binder.registerCustomEditor(Date.class, dateEditor);
    }
}