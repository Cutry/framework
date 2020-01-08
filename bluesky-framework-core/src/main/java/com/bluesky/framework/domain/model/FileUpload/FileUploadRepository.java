package com.bluesky.framework.domain.model.FileUpload;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.FileUpload.FileUpload;

public interface FileUploadRepository {
    /**
     * 上传文件
     * @param fileUpload 文件实体类
     * @return 自增的ID
     */
    Long uploadFile(FileUpload fileUpload);

    /**
     * 删除文件
     * @param id 主键
     * @return  影响行数
     */
    int deleteFile(Long id);

    /**
     * 查询所有的数据
     * @return
     */
    Page<FileUpload> findAllFile(Integer pageNum, Integer pageSize);

    /**
     * 根据主键查询文件
     * @param id 主键
     * @return  文件信息
     */
    FileUpload findFileById(Long id);

    /**
     * 根据类型查找问价
     * @param code 类型
     * @return 文件
     */
    Page<FileUpload> findFileByCode(String code, Integer pageNum, Integer pageSize);
}
