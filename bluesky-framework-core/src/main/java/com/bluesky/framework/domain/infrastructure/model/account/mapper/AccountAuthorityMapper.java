package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.auth.AccountAuthority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountAuthorityMapper {

    /**
     * 保存权限信息
     */
    void saveAuthorities(@Param("accountAuthorityList") List<AccountAuthority> accountAuthorityList);

    /**
     * 根据账户查询权限
     */
    @ResultMap("AccountAuthorityResultMap")
    @Select("select * from account_authority where account_id = #{accountId}")
    List<AccountAuthority> findByAccountId(@Param("accountId") long accountId);

    /**
     * 根据账户删除
     *
     * @param accountId 账户ID
     */
    @Delete("delete from account_authority where account_id = #{accountId}")
    void deleteByAccountId(@Param("accountId") long accountId);

    /**
     * 根据账户、操作删除，当角色删除某个权限时，用户相应的删除
     *
     * @param accountIds 账户ID
     */
    void deleteByAccountIdsAndOperationId(@Param("accountIds") List<Long> accountIds, @Param("operationId") long operationId);
}