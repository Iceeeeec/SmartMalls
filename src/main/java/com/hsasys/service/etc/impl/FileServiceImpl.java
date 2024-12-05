package com.hsasys.service.etc.impl;

import com.hsasys.service.etc.FileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FileServiceImpl implements FileService
{

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传文件到云端
     * @param file
     * @return
     */

    @Override
    public FileInfo uploadMultipartFile(MultipartFile file) {
        // 上传文件路径
        //指定oss保存文件路径 ***/2024/06/18/文件名
        String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
        //上传文件，返回文件信息
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(objectName)
                .upload();
        return fileInfo;
    }

    @Override
    public FileInfo uploadFile(File file) {
        // 上传文件路径
        //指定oss保存文件路径 ***/2024/06/18/文件名
        String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
        //上传文件，返回文件信息
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(objectName)
                .upload();
        return fileInfo;
    }

}
