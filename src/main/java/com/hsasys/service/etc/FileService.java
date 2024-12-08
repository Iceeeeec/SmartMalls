package com.hsasys.service.etc;

import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService
{
    /**
     * 上传文件到云端
     */
    FileInfo uploadMultipartFile(MultipartFile file);

    /**
     * 上传文件
     */
    FileInfo uploadFile(File file);

    FileInfo uploadBytes(byte[] file);

}
