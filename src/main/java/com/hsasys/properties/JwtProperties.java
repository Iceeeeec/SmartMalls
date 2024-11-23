package com.hsasys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hsa.jwt")
@Data
public class JwtProperties
{
    /**
     * 用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private Long userTtl;
    private String userTokenName;
}
