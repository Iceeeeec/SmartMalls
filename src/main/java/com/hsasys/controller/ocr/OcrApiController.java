//package com.hsasys.baiduocr.api;
//
//import com.hsasys.service.ocr.OcrService;
//import com.hsasys.result.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * name
// *
// * @author
// * @date
// */
//@RestController
//@RequestMapping("/api/ocr")
//public class OcrApiController {
//
//    @Autowired
//    private OcrService ocrService;
//
//    /**
//     * 完整路径：http://localhost:80/api/ocr/awardHandle
//     * 表单上传 form-datd
//     * ocr处理奖状
//     *
//     * @param ocr 文件域 file
//     * @return Json返回体
//     */
//    @PostMapping("/awardHandle")
//    public Result awardHandle(@RequestParam("file") MultipartFile ocr) throws Exception {
//        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        byte[] file = null;
//        try {
//            file = ocr.getBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//            jsonObject.put( "code", HttpStatus.INTERNAL_SERVER_ERROR.value() );
//            jsonObject.put( "result", false );
//            jsonObject.put( "message", "上传图片失败" );
//        }
//        try {
//            jsonObject = ocrService.dataHandle( file );
//        } catch (Exception e) {
//            jsonObject.put( "code", HttpStatus.INTERNAL_SERVER_ERROR.value() );
//            jsonObject.put( "result", false );
//            jsonObject.put( "message", "图片识别出错" );
//        }
//        List<String> list = ocrService.ocr_accurateGeneral( file );
//
//
//        return Result.success( list );
//    }
//
//}

package com.hsasys.controller.ocr;

import com.hsasys.service.ocr.OcrService;
import com.hsasys.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
public class OcrApiController {

    @Autowired
    private OcrService ocrService;

    /**
     * 完整路径：http://localhost:9003/api/ocr/awardHandle
     * 表单上传 form-data
     * ocr处理奖状
     *
     * @param ocr 上传的文件
     * @return Json返回体
     */
    @PostMapping("/awardHandle")
    public Result awardHandle(@RequestParam("file") MultipartFile ocr)
    {
        return ocrService.handleOcr(ocr);
    }
}
