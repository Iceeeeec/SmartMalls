package com.hsasys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//前端传入的项目数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalItemDto
{
    //收缩压
    private Measurement systolicPressure;

    //舒张压
    private Measurement diastolicPressure;


    //白细胞
    private Measurement whiteBloodCell;

    //红细胞
    private Measurement redBloodCell;
    //血红蛋白
    private Measurement hemoglobin;
    //血小板
    private Measurement platelet;
    //葡萄糖
    private Measurement glucose;
    //蛋白质
    private Measurement protein;
    //酮体
    private Measurement ketone;
    //谷丙转氨酶
    private Measurement alt;
    //总胆红素
    private Measurement tBil;
    //白蛋白
    private Measurement alb;
    //血糖
    private Measurement bloodSugar;
    //总胆固醇
    private Measurement tCholesterol;
    //甘油三脂
    private Measurement triglyceride;
    //窦性心率
    private Measurement sinusRhythm;
    //肌酐
    private Measurement creatinine;
    //尿素
    private Measurement urea;


    //内嵌一个类，用于接收前端传来的数据
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Measurement
    {
        private Integer id;//项目id
        private Double content;//项目值
        private String name;//项目英文名
    }

    public static List<Measurement> getMeasurementList(PhysicalItemDto physicalItemDto) {
        return List.of(physicalItemDto.getSystolicPressure(),
                physicalItemDto.getDiastolicPressure(),
                physicalItemDto.getWhiteBloodCell(),
                physicalItemDto.getRedBloodCell(),
                physicalItemDto.getHemoglobin(),
                physicalItemDto.getPlatelet(),
                physicalItemDto.getGlucose(),
                physicalItemDto.getProtein(),
                physicalItemDto.getKetone(),
                physicalItemDto.getAlt(),
                physicalItemDto.getTBil(),
                physicalItemDto.getAlb(),
                physicalItemDto.getBloodSugar(),
                physicalItemDto.getTCholesterol(),
                physicalItemDto.getTriglyceride(),
                physicalItemDto.getSinusRhythm(),
                physicalItemDto.getCreatinine(),
                physicalItemDto.getUrea());

    }
}
