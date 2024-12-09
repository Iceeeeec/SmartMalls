package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.PhysicalReport;
import com.hsasys.domain.entity.PhysicalResult;
import com.hsasys.domain.vo.ReportInfoVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReportMapper extends BaseMapper<PhysicalReport>
{

    /**
     * 批量插入体检项目数据
     * @param physicalResultList
     */
    void insertReportItems(List<PhysicalResult> physicalResultList, int userId);

    @Delete("delete from physical_result where user_id = #{userId}")
    void deleteItemsByUserId(int userId);

    @Update("update physical_reports set status = #{status} where user_id = #{userId}")
    void updateReportStatus(@Param("userId") int userId, @Param("status") Integer status);

    List<ReportInfoVo> getReportInfoList(@Param("userId") Integer userId);
}
