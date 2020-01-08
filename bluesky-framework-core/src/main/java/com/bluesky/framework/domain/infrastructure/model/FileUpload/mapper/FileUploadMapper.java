package com.bluesky.framework.domain.infrastructure.model.FileUpload.mapper;

import com.bluesky.framework.FileUpload.FileUpload;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface FileUploadMapper {


    /**
     * 上传文件
     * @param fileUpload 文件实体类
     * @return 自增的ID
     */
    @Insert("insert into tbl_file_upload(code,path,remark,create_time) values(#{file.code}, #{file.path}, #{file.remark}, now())")
    @SelectKey(before = false, keyProperty = "file.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    Long uploadFile(@Param("file") FileUpload fileUpload);

    /**
     * 删除文件
     * @param id 主键
     * @return  影响行数
     */
    @Update("update tbl_file_upload set delete_flag = 1 where id=#{id}")
    int deleteFile(@Param("id") Long id);

    /**
     * 查询所有的数据
     * @return
     */
    @ResultMap("FileUploadMap")
    @Select("select * from tbl_file_upload where delete_flag = 0")
    List<FileUpload> findAllFile();

    /**
     * 根据主键查询文件
     * @param id 主键
     * @return  文件信息
     */
    @ResultMap("FileUploadMap")
    @Select("select * from tbl_file_upload where id=#{id} where delete_flag = 0")
    FileUpload findFileById(@Param("id") Long id);

    /**
     * 根据类型查找问价
     * @param code 类型
     * @return 文件
     */
    @ResultMap("FileUploadMap")
    @Select("select * from tbl_file_upload where code=#{code} and delete_flag=0")
    List<FileUpload> findFileByCode(@Param("code") String code);

    /**
     * 停用一个类型下的所有文件
     * @param code 类型
     */
    @Update("update tbl_file_upload set delete_flag = 1 where code =#{code}")
    void deleteFileByCode(@Param("code") String code);
}
