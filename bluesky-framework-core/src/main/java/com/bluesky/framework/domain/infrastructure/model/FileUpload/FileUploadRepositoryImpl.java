package com.bluesky.framework.domain.infrastructure.model.FileUpload;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.FileUpload.FileUpload;
import com.bluesky.framework.domain.infrastructure.model.FileUpload.mapper.FileUploadMapper;
import com.bluesky.framework.domain.model.FileUpload.FileUploadRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileUploadRepositoryImpl implements FileUploadRepository {

    @Autowired
    FileUploadMapper fileUploadMapper;

    @Override
    public Long uploadFile(FileUpload fileUpload) {
        fileUploadMapper.deleteFileByCode(fileUpload.getCode());
        fileUploadMapper.uploadFile(fileUpload);
        return fileUpload.getId();
    }

    @Override
    public int deleteFile(Long id) {
        return fileUploadMapper.deleteFile(id);
    }

    @Override
    public Page<FileUpload> findAllFile(Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 15;
        PageHelper.startPage(pageNum, pageSize);
        List<FileUpload> list = fileUploadMapper.findAllFile();
        return PageBeanUtils.copyPageProperties(list);
    }


    @Override
    public FileUpload findFileById(Long id) {
        return fileUploadMapper.findFileById(id);
    }

    @Override
    public Page<FileUpload> findFileByCode(String code, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 15;
        PageHelper.startPage(pageNum, pageSize);
        List<FileUpload> list = fileUploadMapper.findFileByCode(code);
        return PageBeanUtils.copyPageProperties(list);
    }

}
