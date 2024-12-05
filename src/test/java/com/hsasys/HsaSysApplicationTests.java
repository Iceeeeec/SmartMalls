package com.hsasys;

import com.hsasys.dao.domain_mapper.AllergenMapper;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.etc.FileService;
import com.hsasys.service.select.PhysicalService;
import com.hsasys.service.select.SUserService;
import com.hsasys.utils.PdfToImage;
import org.dromara.x.file.storage.core.FileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
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
    private FileService fileService;

    @Autowired
    private AllergenMapper allergenMapper;
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
    void test_uploadImage() throws IOException {
        File file = PdfToImage.convertSinglePagePdfToImage("/Users/wulinxin/Desktop/2022051615222武琳鑫发票.pdf");
        FileInfo fileInfo = fileService.uploadFile(file);
        System.out.println(fileInfo.getUrl());
    }
}
