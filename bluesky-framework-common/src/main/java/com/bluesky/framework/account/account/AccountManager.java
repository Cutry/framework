package com.bluesky.framework.account.account;

import com.bluesky.common.vo.Page;

import java.util.List;


public interface AccountManager {

    Long insert(Account account);


    Account findOne(Long id);

    /**
     * 通过手机号查找用户
     * @param mobile
     * @return
     */
    Account findOneByMobile(String mobile);

    /**
     * 根据角色id查询所有用户id
     *
     * @param roleId 角色id
     * @return 所有的用户ids
     */
    List<Long> findIdsByRoleId(Long roleId);


    /**
     * 根据手机号查找
     *
     * @param mobile   手机号
     * @param roleType 角色类型
     * @see com.bluesky.framework.account.constant.RoleType
     */
    List<Account> findByMobile(String mobile, Integer... roleType);


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
    void updatePassword(Long id, String password);


    /**
     * 条件查询
     *
     * @param name           姓名
     * @param mobile         手机号
     * @param organizationId 单位id
     * @param roleId         角色id
     * @param status         状态
     * @see com.bluesky.framework.account.constant.AccountStatus
     */
    Page<Account> findPageByCondition(String name, String mobile, Long organizationId, Long roleId, Integer status, Integer pageNum, Integer pageSize);

    void restPasswrd(String mobile);

    Account findOneByMobile2(String phone);
}