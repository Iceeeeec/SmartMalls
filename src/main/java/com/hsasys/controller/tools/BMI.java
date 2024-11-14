package com.hsasys.controller.tools;


public class BMI {
    public static final Integer THIN = 1;
    public static final Integer NORMAL = 2;
    public static final Integer OVERWEIGHT = 3;
    public static final Integer OBESITY = 4;


    public static double calculateBMI(double weight, double height) {
        // 体重（kg）除以身高（m）的平方
        return weight / (height * height) * 10000;
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
