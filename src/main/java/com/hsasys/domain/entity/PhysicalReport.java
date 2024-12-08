package com.hsasys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * (PhysicalReports)表实体类
 *
 * @author makejava
 * @since 2024-12-07 14:49:09
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("physical_reports")
public class PhysicalReport
{
    @TableId
    private Integer id;

    private Integer userId;
    //报告路径
    private String filePath;
    //上传时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadDate;
    //审核状态
    private Integer status;

}
