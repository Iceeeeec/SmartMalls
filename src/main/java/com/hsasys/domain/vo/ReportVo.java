package com.hsasys.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportVo
{
    //用户名字
    private String userName;

    private String filePath;

    private LocalDateTime uploadDate;

    //审核状态
    private Integer status;
}
