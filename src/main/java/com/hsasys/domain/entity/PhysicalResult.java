package com.hsasys.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (PhysicalResult)表实体类
 *
 * @author makejava
 * @since 2024-12-07 17:41:57
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("physical_result")
public class PhysicalResult
{
    @TableId
    private Long id;

    //用户id
    private Integer userId;
    //体检项目id
    private Integer itemId;
    //含量
    private Double content;
    //状态
    private Integer status;

}
