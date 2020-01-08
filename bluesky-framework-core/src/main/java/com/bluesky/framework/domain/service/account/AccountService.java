package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.account.Account;

import java.util.List;

public interface AccountService {
    long insert(Account account);


    void update(Account account);


    /**
     * 启用账户
     *
     * @param ids 账号id
     */
    void enable(List<Long> ids);


    /**
     * 禁用账户
     *
     * @param ids 账户id
     */
    void disable(List<Long> ids);


    /**
     * 删除账户
     *
     * @param ids 账户ids
     */
    void delete(List<Long> ids);


    /**
     * 重置密码
     *
     * @param id       主键
     * @param password 密码
     */
    void updatePassword(long id, String password);


    /**
     * 用户名/手机号登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param roleType 角色类型
     * @return 返回账户
     * @see com.bluesky.framework.account.constant.RoleType
     */
    Account loginUserName(String userName, String password, List<Integer> roleType, String ip) throws Exception;

    void restPasswrd(String mobile);
}