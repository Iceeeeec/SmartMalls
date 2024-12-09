package com.hsasys;

import com.hsasys.dao.domain_mapper.AllergenMapper;
import com.hsasys.dao.domain_mapper.PhysicalMapper;
import com.hsasys.dao.domain_mapper.PhysicalResultMapper;
import com.hsasys.dao.domain_mapper.ReportMapper;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.domain.vo.ReportInfoVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.etc.FileService;
import com.hsasys.service.select.PhysicalService;
import com.hsasys.service.select.SUserService;
import com.hsasys.utils.ConvertUtils;
import com.spire.doc.FileFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class HsaSysApplicationTests {
    @Autowired
    private SUserService userService;

    @Autowired
    private PhysicalService physicalService;
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private AllergenMapper allergenMapper;

    @Autowired
    private PhysicalResultMapper resultMapper;
    @Test
    void test_login(){
        UserLoginDto user = new UserLoginDto();
        user.setUsername("root");
        user.setPassword("123456");
        Result<UserLoginVo> login = userService.login(user);
        System.out.println(login);
    }

    @Test
    void test_rel(){
        List<Allergen> allergens = allergenMapper.selectList(null);
        System.out.println(allergens);
    }

    @Test
    void test_list()
    {
        System.out.println(Arrays.asList(4, 3, 2));
    }


    @Test
    void test_physical()
    {
        Result<List<PhysicalType>> listResult = physicalService.selectTypes();
        System.out.println(listResult);
        Result<List<PhysicalItemVo>> list = physicalService.selectItemByid(1);
        System.out.println(list);
    }

    @Test
    void test_items()
    {
        System.out.println(physicalService.selectItems());
    }

    @Test
    void test_uploadImage() throws IOException
    {
    }
    @Test
    void test_getReportInfo()
    {
        List<ReportInfoVo> reportInfoList = reportMapper.getReportInfoList(28);
        System.out.println(reportInfoList);
    }
    @Test
    void test_update()
    {

    }
}
