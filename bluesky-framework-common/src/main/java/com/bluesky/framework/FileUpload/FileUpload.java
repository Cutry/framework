package com.bluesky.framework.FileUpload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @Description TODO 上传文件信息实体
 *               每次添加新类型文件需要在code的注释上添加代码以及中文注释
 * @Author Raindrop
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 文件类型
     *          template_finance        --我要融资模板
     */
    private String code;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件备注
     */
    private String remark;

    /**
     * 删除标志位
     */
    private Boolean deleteFlag;
    /**
     * 上传日期
     */
    private String createTime;
}
