package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.account.Account;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.security.access.method.P;

import java.util.List;

public interface AccountMapper {
    @Insert("insert into account " +
            "(name, nick_name,head_img, mobile,role_type, role_id, organization_id, department, id_card, password, last_login_ip, last_login_time, authorities_text, sort, status, created_at, updated_at, yxdw_id, company_id) " +
            "values " +
            "(#{account.name},#{account.nickName},#{account.headImg},#{account.mobile},#{account.roleType},#{account.roleId},#{account.organizationId},#{account.department},#{account.idCard},#{account.password},#{account.lastLoginIp},#{account.lastLoginTime},#{account.authoritiesText},#{account.sort},#{account.status},now(),now(),#{account.yxdwId},#{account.companyId})")
    @SelectKey(before = false, keyProperty = "account.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("account") Account account);


    @ResultMap("AccountResultMap")
    @Select("select * from account where id = #{id}")
    Account findOne(@Param("id") Long id);

    /**
     * 通过手机号查找用�?
     * @param mobile
     * @return
     */
    @ResultMap("AccountResultMap")
    @Select("select * from account where mobile = #{mobile}")
    Account findOneByMobile(@Param("mobile") String mobile);

    /**
     * 根据手机号查�?
     *
     * @param mobile   手机
     * @param roleType 角色类型
     * @see com.bluesky.framework.account.constant.RoleType
     */
    @ResultMap("AccountResultMap")
    List<Account> findByMobile(@Param("mobile") String mobile, @Param("roleType") List<Integer> roleType);


    /**
     * 根据角色id查询�?有用户id
     *
     * @param roleId 角色id
     * @return �?有的用户ids
     */
    @Select("select id from account where role_id = #{roleId} and status != -15")
    List<Long> findIdsByRoleId(@Param("roleId") long roleId);


    void update(@Param("account") Account account);


    /**
     * 批量修改状�??
     *
     * @param ids    账号id
     * @param status 新状�?
     */
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") int status);


    /**
     * 批量修改状�??
     *
     * @param mobiles   账号mobile
     * @param status    新状�?
     */
    void batchUpdateStatusByMobile(@Param("mobiles") List<Long> mobiles, @Param("status") int status);


    /**
     * 条件查询
     *
     * @param name           姓名
     * @param mobile         手机�?
     * @param organizationId 单位id
     * @param roleId         角色id
     * @param status         状�??
     * @see com.bluesky.framework.account.constant.AccountStatus
     */
    List<Account> findByCondition(@Param("name") String name, @Param("mobile") String mobile, @Param("organizationId") Long organizationId, @Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 停用/启用企业账号
     * @param companyId 公司主键
     * @param status    状�??
     */
    @Update("update account set status = #{status} where company_id = #{companyId}")
    void UpdateStatusByCompanyId(@Param(("companyId")) Long companyId, @Param("status") int status);

    /**
     * 更新企业密码
     * @param companyId     企业主键
     * @param password      初始密码
     */
    @Update("update account set password = #{password} where company_id = #{companyId}")
    void initPasswordByCompanyId(@Param("companyId") Long companyId, @Param("password") String password);

    @Update("update account set password=#{password} where mobile = #{mobile}")
    void restPasswrd(@Param("mobile") String mobile,@Param("password") String password);

    @Update("update account set status=#{status} where mobile = #{mobile}")
    void updateStatusByMobile(@Param("mobile") String mobile,@Param("status") int status);

    @Select("select id,mobile,yxdw_id as yxdwId,company_id as companyId from account where mobile = #{mobile}")
    Account findOneByMobile2(@Param("mobile") String mobile);;
}