package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.log.AccountLoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface AccountLoginLogMapper {
    @Insert("insert into account_login_log " +
            "(account_id, account_name, organization_id, organization_name, mobile, ip, ip_area,type, success, memo, created_at, updated_at) " +
            "values " +
            "(#{loginLog.accountId},#{loginLog.accountName},#{loginLog.organizationId},#{loginLog.organizationName},#{loginLog.mobile},#{loginLog.ip},#{loginLog.ipArea},#{loginLog.type},#{loginLog.success},#{loginLog.memo},now(),now())")
    @SelectKey(before = false, keyProperty = "loginLog.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("loginLog") AccountLoginLog loginLog);


    List<AccountLoginLog> findByCondition(@Param("accountName") String accountName, @Param("type") Integer type, @Param("success") Integer success);
}