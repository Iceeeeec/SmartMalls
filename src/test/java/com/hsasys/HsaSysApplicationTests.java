package com.hsasys;

import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.mapper.AllergenMapper;
import com.hsasys.mapper.ReportMapper;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.domain.vo.ReportInfoVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.etc.FileService;
import com.hsasys.service.select.FoodService;
import com.hsasys.service.select.PhysicalService;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.select.impl.FoodServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class HsaSysApplicationTests {
//    @Autowired
//    private SUserService userService;
//
    @Autowired
    private FoodServiceImpl foodService;
//    @Autowired
//    private PhysicalService physicalService;
//    @Autowired
//    private ReportMapper reportMapper;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private AllergenMapper allergenMapper;
//
//
//    @Test
//    void test_login(){
//        UserLoginDto user = new UserLoginDto();
//        user.setUsername("root");
//        user.setPassword("123456");
//        Result<UserLoginVo> login = userService.login(user);
//        System.out.println(login);
//    }
//
//    @Test
//    void test_rel(){
//        List<Allergen> allergens = allergenMapper.selectList(null);
//        System.out.println(allergens);
//    }
//
//    @Test
//    void test_list()
//    {
//        System.out.println(Arrays.asList(4, 3, 2));
//    }
//
//
//    @Test
//    void test_physical()
//    {
//        Result<List<PhysicalType>> listResult = physicalService.selectTypes();
//        System.out.println(listResult);
//        Result<List<PhysicalItemVo>> list = physicalService.selectItemByid(1);
//        System.out.println(list);
//    }
//
//    @Test
//    void test_items()
//    {
//        System.out.println(physicalService.selectItems());
//    }
//
//    @Test
//    void test_uploadImage() throws IOException
//    {
//    }
//    @Test
//    void test_getReportInfo()
//    {
//        List<ReportInfoVo> reportInfoList = reportMapper.getReportInfoList(28);
//        System.out.println(reportInfoList);
//    }
//    @Test
//    void test_list1()
//    {
//        ArrayList<String> s1 = new ArrayList<>();
//        s1.add("123");
//        s1.add("1233");
//        String s2 = "123";
//        System.out.println(s1.contains(s2));
//    }
//
//
//    public static Double extractDoubleFromContent(String content) {
//        // 无效值直接返回 null
//        if (content == null || content.trim().equals("-") || content.trim().equals("Tr"))
//        {
//            return 0.0;
//        }
//
//        // 正则提取数字部分，包括整数和小数
//        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
//        Matcher matcher = pattern.matcher(content);
//
//        // 如果找到数字，则转换为 double 返回
//        if (matcher.find()) {
//            String number = matcher.group();
//            return Double.parseDouble(number);
//        }
//
//        // 没有找到数字时返回 null
//        return 0.0;
//    }
//
//    @Autowired
//    private FoodService foodService;
//    @Test
//    void test_pageFood()
//    {
//        FoodPageDto foodPageDto = new FoodPageDto(100, 5, 2, null);
//
//        Result<PageResult> pageResultResult = foodService.selectFoodsByQuery(foodPageDto);
//        System.out.println(pageResultResult);
//    }
//
//    @Test
//    void test_foodById()
//    {
//        Result<FoodDetailVo> foodDetailVoResult = foodService.selectFoodById(188);
//        System.out.println(foodDetailVoResult);
//    }
    @Test
    void recommendTest()
    {
        foodService.addRecommendFood(28L);
    }
}
