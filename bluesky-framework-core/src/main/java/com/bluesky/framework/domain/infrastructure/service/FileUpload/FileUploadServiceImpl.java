package com.bluesky.framework.domain.infrastructure.service.FileUpload;

import com.bluesky.framework.domain.model.FileUpload.FileUploadRepository;
import com.bluesky.framework.domain.service.FileUpload.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    FileUploadRepository fileUploadRepository;

}
