package com.bluesky.framework.api.controller.FileUpload;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.FileUpload.FileUpload;
import com.bluesky.framework.FileUpload.FileUploadManager;
import com.bluesky.framework.account.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 记录上传的文件
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    FileUploadManager fileUploadManager;

    /**
     * 上传文件 需要确定一下如何上传，表单提交方式还是什么的
     *
     * @param fileUpload
     * @return
     */
    @PostMapping("uploadFile")
    @PreAuthorize("hasPermission('crm-api','sm_authorization_file')")
    public DataResponse uploadFile(@AuthenticationPrincipal Account account,
                                   HttpServletRequest request,
                                   @RequestBody FileUpload fileUpload) {
        DataResponse dataResponse = new DataResponse();
        if (fileUploadManager.uploadFile(fileUpload) > 0) {
            dataResponse.setMessage("添加成功");
            return dataResponse;
        } else {
            return dataResponse.setFalseAndMessage("服务器出错，请稍后重试");
        }
    }

    /**
     * 根据类型查找文件
     * @param code      类型
     * @param pageNum   页码
     * @param pageSize  每页显示数量
     * @return
     */
    @GetMapping("/findByCode")
    @PreAuthorize("hasPermission('crm-api','sm_authorization_file')")
    public DataResponse findByCode(@AuthenticationPrincipal Account account,
                                   HttpServletRequest request,
                                   @RequestParam("code") String code,
                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        DataResponse dataResponse = new DataResponse();
        Page<FileUpload> page = fileUploadManager.findFileByCode(code, pageNum, pageSize);
        dataResponse.addData("page", page);
        return dataResponse;
    }

    /**
     *  查找所有的文件
     * @param pageNum   页码
     * @param pageSize  每页显示数量
     * @return
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasPermission('crm-api','sm_authorization_file')")
    public DataResponse findAll(@AuthenticationPrincipal Account account,
                                HttpServletRequest request,
                                @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        DataResponse dataResponse = new DataResponse();
        Page<FileUpload> page = fileUploadManager.findAllFile(pageNum, pageSize);
        dataResponse.addData("page", page);
        return dataResponse;
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @GetMapping("delete")
    @PreAuthorize("hasPermission('crm-api','sm_authorization_file')")
    public DataResponse deleteFile(@AuthenticationPrincipal Account account,
                                   HttpServletRequest request,
                                   @RequestParam("id") Long id){
        DataResponse dataResponse = new DataResponse();
        if (fileUploadManager.deleteFile(id)){
            dataResponse.setMessage("删除成功");
        }else{
            dataResponse.setFalseAndMessage("数据库中无此记录");
        }

        return dataResponse;
    }
}
