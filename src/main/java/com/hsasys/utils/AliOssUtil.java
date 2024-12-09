package com.hsasys.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.VoidResult;
import com.hsasys.properties.AliOssProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil
{

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String domain;


    /**
     * 删除单个文件
     */
    public boolean deleteFile(String fileName)
    {
        /**
         * 填写文件名。文件名包含路径，不包含Bucket名称。
         * 例如2021/09/14/52c6a3114e634979a2934f1ea12deaadfile.png。
         */
        String objectName = fileName.replace(domain, "");

        boolean flag = false;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
            // 删除文件。
            VoidResult voidResult = ossClient.deleteObject(bucketName, objectName);
            if (voidResult.getResponse().getStatusCode() == 204)
            {
                flag = true;
            }
            // 关闭OSSClient。
            ossClient.shutdown();

        } catch (OSSException e){
            e.printStackTrace();
        }
        return flag;
    }
}
