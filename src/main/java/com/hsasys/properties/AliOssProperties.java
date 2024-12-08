package com.hsasys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hsa.alioss")
@Data
public class AliOssProperties {

    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String domain;
}
