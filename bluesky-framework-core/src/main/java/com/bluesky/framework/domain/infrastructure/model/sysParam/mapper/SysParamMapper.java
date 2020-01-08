package com.bluesky.framework.domain.infrastructure.model.sysParam.mapper;

import com.bluesky.framework.sysParam.SysParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface SysParamMapper {

    @Insert("insert into sys_param " +
            "(param, mark, `desc`, sort, isleaf) " +
            "values " +
            "(#{param.param},#{param.mark},#{param.desc},#{param.sort},'0')")
    @SelectKey(before = false, keyProperty = "param.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    Long insert(@Param("param") SysParam param);

    @ResultMap("SysParamResultMap")
    @Select("select * from sys_param where id = #{id}")
    SysParam findOne(@Param("id") Long id);

    @ResultMap("SysParamResultMap")
    @Select("select * from sys_param where mark = #{mark} order by sort")
    List<SysParam> findByMark(@Param("mark") String mark);

    @Delete("delete from sys_param where id = #{id}")
    void delete(@Param("id") Long id);

    @Update("update sys_param set param=#{param.param}, sort=#{param.sort}, `desc`=#{param.desc} where id=#{param.id}")
    void update(@Param("param") SysParam param);

    @Select("select * from sys_param where mark= #{mark} order by sort")
    List<SysParam> findAll(@Param("mark") String mark);

}
