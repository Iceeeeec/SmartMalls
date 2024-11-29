package com.hsasys.utils;


public class BMI {
    public static final Integer THIN = 1;
    public static final Integer NORMAL = 2;
    public static final Integer OVERWEIGHT = 3;
    public static final Integer OBESITY = 4;


    public static double calculateBMI(double weight, double height)
    {
        // 计算 BMI 并格式化保留两位小数
        double bmi = weight / (height * height) * 10000;
        return Double.parseDouble(String.format("%.2f", bmi));
    }

    public static Integer interpretBMI(double bmi) {
        if (bmi < 18.5) {
            return THIN;
        } else if (bmi < 24.9) {
            return NORMAL;
        } else if (bmi < 29.9) {
            return OVERWEIGHT;
        } else {
            return OBESITY;
        }
    }
}
