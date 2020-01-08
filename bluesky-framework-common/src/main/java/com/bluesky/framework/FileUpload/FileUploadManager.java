package com.bluesky.framework.FileUpload;

import com.bluesky.common.vo.Page;

/*
 * @Description TODO 文件上传管理的Manager类
 * @Author Raindrop
 * @Date 2019/7/29
 */
public interface FileUploadManager {
    /**
     * 上传文件
     * @param fileUpload 文件实体类
     * @return 自增的ID
     */
    Long uploadFile(FileUpload fileUpload);

    /**
     * 删除文件
     * @param id 主键
     * @return  插入结果
     */
    boolean deleteFile(Long id);

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
