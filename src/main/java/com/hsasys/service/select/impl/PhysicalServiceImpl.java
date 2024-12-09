package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.constant.AppHttpCodeEnum;
import com.hsasys.context.BaseContext;
import com.hsasys.dao.domain_mapper.PhysicalMapper;
import com.hsasys.dao.domain_mapper.ReportMapper;
import com.hsasys.dao.domain_mapper.UserMapper;
import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.*;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.exception.ReportGenerateException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.result.Result;
import com.hsasys.service.etc.FileService;
import com.hsasys.service.select.PhysicalService;
import com.hsasys.utils.AliOssUtil;
import com.hsasys.utils.BeanCopyUtils;
import com.hsasys.utils.ConvertUtils;
import com.hsasys.utils.WordUtil;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhysicalServiceImpl implements PhysicalService
{

    private final String TEMP_PATH = "src/main/resources/static/ReportTemplate.docx";
    @Autowired
    private PhysicalMapper physicalMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AliOssUtil aliOssUtil;


    @Autowired
    private FileService fileService;

    @Autowired
    private ReportMapper reportMapper;
    @Override
    public Result<List<PhysicalType>> selectTypes()
    {
        List<PhysicalType> types= physicalMapper.selectTypes();
        return Result.success(types);
    }

    @Override
    public Result<List<PhysicalItemVo>> selectItemByid(Integer id)
    {
        LambdaQueryWrapper<PhysicalItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhysicalItem::getTypeId, id);
        List<PhysicalItem> physicalItems = physicalMapper.selectList(wrapper);
        PhysicalType type = physicalMapper.selectTypeById(id);
        List<PhysicalItemVo> physicalItemVos = BeanCopyUtils.copyBeanList(physicalItems, PhysicalItemVo.class);
        physicalItemVos.forEach(item -> item.setPhysicalType(type));
        return Result.success(physicalItemVos);
    }

    @Override
    public Result<List<PhysicalItem>> selectItems()
    {
        return Result.success(physicalMapper.selectList(null));
    }

    /**
     * 生成体检报告
     * @param physicalItemDto
     * @return
     */
    @Override
    @Transactional
    public Result createReport(PhysicalItemDto physicalItemDto)
    {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        System.out.println(userId);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
        {
            //TODO:定义常量
            throw new UserIsExistException("用户信息缺失，请重试");
        }
        //生成报告
        //基本信息
        Map<String, String> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("sex", user.getSex());
        map.put("age", String.valueOf(user.getAge()));
        //时间
        map.put("year", String.valueOf(LocalDate.now().getYear()));
        map.put("mouth", String.valueOf(LocalDate.now().getMonthValue()));
        map.put("day", String.valueOf(LocalDate.now().getDayOfMonth()));
        //体检项目
        List<PhysicalItemDto.Measurement> measurementList = PhysicalItemDto.getMeasurementList(physicalItemDto);


        for (PhysicalItemDto.Measurement measurement : measurementList)
        {
            if(measurement != null)
            {
                Double content = measurement.getContent();
                String value = String.valueOf(content);
                if(content == null)
                {
                    value = "暂无";
                }
                map.put(measurement.getName(), String.valueOf(value));
            }
            else
            {
                throw new ReportGenerateException("体检项目生成失败，请重试");
            }
        }

        //输入模版
        try
        {
            byte[] file = WordUtil.generateWordBytes(TEMP_PATH, map);
            file = ConvertUtils.wordToPdf(file);
            //上传pdf
            FileInfo PdfFileInfo = fileService.uploadBytes(file);
            String pdfUrl = PdfFileInfo.getUrl();
            //保存到数据库
            PhysicalReport report = PhysicalReport.builder()
                    .filePath(PdfFileInfo.getUrl())
                    .userId(user.getId())
                    .uploadDate(LocalDateTime.now())
                    //TODO:定义常量 0为未审核 1未已审核
                    .status(0)
                    .build();
            //将报告pdf保存在数据库
            reportMapper.insert(report);
            file = ConvertUtils.convertSinglePagePdfToImage(file);
            //上传图片
            FileInfo fileInfo = fileService.uploadBytes(file);
            Map<String, String> res = new HashMap<>();
            res.put("pdfUrl", pdfUrl);
            res.put("imageUrl", fileInfo.getUrl());
            return Result.success(res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Result saveReport(PhysicalItemDto physicalItemDto)
    {
        Long id = BaseContext.getCurrentId();
        int userId = id.intValue();
        reportMapper.deleteItemsByUserId(userId);
        List<PhysicalItemDto.Measurement> measurementList = PhysicalItemDto.getMeasurementList(physicalItemDto);
        for (PhysicalItemDto.Measurement measurement : measurementList)
        {
            //判断各个项目的状态
            updateResultStatus(measurement.getId(), measurement.getContent(), userId);
        }
        //改变体检报告状态
        //TODO:定义常量
        reportMapper.updateReportStatus(userId, 1);
        return Result.success();
    }

    /**
     * 取消体检报告
     * @return
     */
    @Override
    public Result cancelReport()
    {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<PhysicalReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhysicalReport::getUserId, userId);
        //TODO:定义常量 0为未审核 1未已审核
        wrapper.eq(PhysicalReport::getStatus, 0);
        List<PhysicalReport> reports = reportMapper.selectList(wrapper);
        //TODO 将oss中的没用的文件删除，暂定
        //将oss中的文件删除了
        for(PhysicalReport report : reports)
        {
            aliOssUtil.deleteFile(report.getFilePath());
        }
        reportMapper.delete(wrapper);
        return Result.success();
    }

    /**
     * 根据体检项目id和内容更新体检结果状态
     * @param itemId
     * @param content
     * @param userId
     */
    private void updateResultStatus(Integer itemId, Double content, Integer userId)
    {
        LambdaQueryWrapper<PhysicalItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhysicalItem::getId, itemId);
        PhysicalItem item = physicalMapper.selectOne(wrapper);
        String normalRange = item.getNormalRange();
        String[] split = normalRange.split("-");
        double min = Double.parseDouble(split[0]);
        double max = Double.parseDouble(split[1]);
        PhysicalResult physicalResult = PhysicalResult.builder()
                .itemId(itemId)
                .userId(userId)
                .content(content).build();
        if(content == null)
        {
            //用户，当正常
            physicalResult.setStatus(1);
        }
        else
        {
            if(content < min)
            {
                //TODO 定义具体状态常量, 0为低， 1为正常，2为高
                physicalResult.setStatus(0);
            }
            else if(content > max)
            {
                physicalResult.setStatus(2);
            }
            else
            {
                physicalResult.setStatus(1);
            }
        }
        physicalMapper.insertResult(physicalResult);
    }
}
