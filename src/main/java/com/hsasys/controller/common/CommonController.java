package com.hsasys.controller.common;

import com.hsasys.result.Result;
import com.hsasys.service.etc.FileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController
{
    @Autowired
    private FileService fileService;
    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            FileInfo fileInfo = fileService.uploadMultipartFile(file);
            Map<String, Object> map = new HashMap<>();
            map.put("url", fileInfo.getUrl());
            map.put("originalFilename", file.getOriginalFilename());
            return Result.success(map);
        }
        catch (Exception e)
        {
            return Result.error(e.getMessage());
        }
    }
}
