//package com.hsasys.baiduocr.api;
//
//import com.hsasys.baiduocr.service.OcrService;
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

package com.hsasys.baiduocr.api;

import com.hsasys.baiduocr.service.OcrService;
import com.hsasys.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public Result awardHandle(@RequestParam("file") MultipartFile ocr) {
        // 返回的JSON对象
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        byte[] file = null;

        // 处理上传文件
        try {
            file = ocr.getBytes();
        } catch (IOException e) {
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "上传文件失败");
            return Result.error(jsonObject.toJSONString());
        }

        // 处理OCR识别
        try {
            // 判断上传的文件类型，PDF或者图片
            String fileName = ocr.getOriginalFilename();
            assert fileName != null;
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            // 如果是PDF文件，进行PDF OCR处理
            if (fileExtension.equals("pdf")) {
                List<String> result = ocrService.ocrPdf(file);
                return Result.success(result);
            }
            // 如果是图片，进行图片OCR处理
            else {
                List<String> result = ocrService.ocr_accurateGeneral(file);
                return Result.success(result);
            }

        } catch (Exception e) {
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "OCR处理失败：" + e.getMessage());
            return Result.error(jsonObject.toJSONString());
        }
    }
}
