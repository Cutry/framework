package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.log.AccountOperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;

import java.util.Date;
import java.util.List;

public interface AccountOperateLogMapper {
    @Insert("insert into account_operate_log " +
            "(account_id, account_name, organization_id, organization_name, mobile, ip,ip_area, step_code, step, memo, created_at, updated_at) " +
            "values " +
            "(#{operateLog.accountId},#{operateLog.accountName},#{operateLog.organizationId},#{operateLog.organizationName},#{operateLog.mobile},#{operateLog.ip},#{operateLog.ipArea},#{operateLog.stepCode},#{operateLog.step},#{operateLog.memo},#{operateLog.createdAt},now())")
    @SelectKey(before = false, keyProperty = "operateLog.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("operateLog") AccountOperateLog operateLog);


    List<AccountOperateLog> findByCondition(@Param("accountName") String accountName, @Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd);

}