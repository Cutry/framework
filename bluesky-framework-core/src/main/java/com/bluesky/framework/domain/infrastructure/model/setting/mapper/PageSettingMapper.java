package com.bluesky.framework.domain.infrastructure.model.setting.mapper;

import com.bluesky.framework.setting.page.PageSetting;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface PageSettingMapper {
    @Insert("insert into page_setting " +
            "(page, type, value, operator_id, created_at, updated_at) " +
            "values " +
            "(#{pageSetting.page},#{pageSetting.type},#{pageSetting.value},#{pageSetting.operatorId},now(),now())")
    @SelectKey(before = false, keyProperty = "pageSetting.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void save(@Param("pageSetting") PageSetting pageSetting);

    @ResultMap("PageSettingResultMap")
    @Select("select * from page_setting where id = #{id}")
    PageSetting findOne(@Param("id") long id);

    @ResultMap("PageSettingResultMap")
    @Select("select * from page_setting order by page")
    List<PageSetting> findAll();

    @ResultMap("PageSettingResultMap")
    @Select("select * from page_setting where page = #{page}")
    List<PageSetting> findByPage(@Param("page") String page);

    /**
     * 更新Value
     */
    @Update("update page_setting set value=#{pageSetting.value} where id=#{pageSetting.id}")
    void updateValue(@Param("pageSetting") PageSetting pageSetting);

}