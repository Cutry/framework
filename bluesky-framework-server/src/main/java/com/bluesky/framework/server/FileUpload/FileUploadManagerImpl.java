package com.bluesky.framework.server.FileUpload;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.FileUpload.FileUpload;
import com.bluesky.framework.FileUpload.FileUploadManager;
import com.bluesky.framework.domain.model.FileUpload.FileUploadRepository;
import com.bluesky.framework.domain.service.FileUpload.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DubboService
public class FileUploadManagerImpl implements FileUploadManager {
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    FileUploadRepository fileUploadRepository;

    @Override
    public Long uploadFile(FileUpload fileUpload) {
        return fileUploadRepository.uploadFile(fileUpload);
    }

    @Override
    public boolean deleteFile(Long id) {
        return fileUploadRepository.deleteFile(id) == 1 ? true : false;
    }

    @Override
    public Page<FileUpload> findAllFile(Integer pageNum, Integer pageSize) {
        return fileUploadRepository.findAllFile(pageNum, pageSize);
    }

    @Override
    public FileUpload findFileById(Long id) {
        return fileUploadRepository.findFileById(id);
    }

    @Override
    public Page<FileUpload> findFileByCode(String code, Integer pageNum, Integer pageSize) {
        return fileUploadRepository.findFileByCode(code, pageNum ,pageSize);
    }
}
