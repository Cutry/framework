package com.bluesky.framework.domain.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 账户管理Repository
 */
public interface AccountRepository {

    long insert(Account account);


    Account findOne(long id);

    /**
     * 通过手机号查找用户
     * @param mobile
     * @return
     */
    Account findOneByMobile(String mobile);


    void update(Account account);


    /**
     * 根据手机号查找
     *
     * @param mobile   手机
     * @param roleType 角色类型
     * @see com.bluesky.framework.account.constant.RoleType
     */
    List<Account> findByMobile(String mobile, Integer... roleType);


    /**
     * 根据角色id查询所有用户id
     *
     * @param roleId 角色id
     * @return 所有的用户ids
     */
    List<Long> findIdsByRoleId(long roleId);


    /**
     * 批量修改状态
     *
     * @param mobiles   账号mobile
     * @param status    新状态
     */
    void batchUpdateStatusByMobile(List<Long> mobiles, int status);


    /**
     * 批量修改状态
     *
     * @param ids    账号id
     * @param status 新状态
     */
    void batchUpdateStatus(@Param("ids") List<Long> ids, int status);


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


    /**
     * 停用企业账号
     * @param companyId 公司主键
     * @param status    状态
     */
    void UpdateStatusByCompanyId(Long companyId, int status);

    /**
     * 重置企业登录密码
     * @param companyId
     */
    void initPasswordByCompanyID(Long companyId);

    void restPasswrd(String mobile, String password);

    void updateStatusByMobile(String mobile, int status);

    Account findOneByMobile2(String phone);
}
