package com.hsasys.config;



import com.hsasys.properties.AliOssProperties;
import com.hsasys.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类, 用于创建AliOssUtil对象 
 */
@Configuration
@Slf4j
public class OssConfiguration
{
    @Bean
    @ConditionalOnMissingBean//如果容器中没有AliOssUtil对象, 就只 创建一个
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties)
    {
        log.info("开始创建阿里云文件上传工具类:{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndPoint(),
                aliOssProperties.getAccessKey(),
                aliOssProperties.getSecretKey(),
                aliOssProperties.getBucketName(),
                aliOssProperties.getDomain());
    }
}
