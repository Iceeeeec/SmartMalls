package com.hsasys.baiduocr.api;

import com.hsasys.baiduocr.service.OcrService;
import com.hsasys.baiduocr.service.foodaddService;
import com.hsasys.controller.tools.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hsasys.controller.tools.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * name
 *
 * @author
 * @date
 */
@RestController
@RequestMapping("/api/ocr")
public class OcrApiController {

    @Autowired
    private OcrService ocrService;
    @Autowired
    private foodaddService foodaddService;

    /**
     * 完整路径：http://localhost:80/api/ocr/awardHandle
     * 表单上传 form-datd
     * ocr处理奖状
     *
     * @param ocr 文件域 file
     * @return Json返回体
     */
    @PostMapping("/awardHandle")
    public Result awardHandle(@RequestParam("file") MultipartFile ocr) throws Exception {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        byte[] file = null;
        try {
            file = ocr.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "上传图片失败");
        }
        try {
            jsonObject = ocrService.dataHandle(file);
        } catch (Exception e) {
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "图片识别出错");
        }
        List<String> list= ocrService.ocr_accurateGeneral( file );

        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(" ");
        }
        String result = sb.toString().trim();
        result=result.replaceAll( "\\s+","" );
        List<String> myList = new ArrayList<>();
        String[] splitStrings = result.split("、");
        for(String item:splitStrings){
            myList.add( item );
        }

        for(int i=0;i< myList.size();i++){
            System.out.println(myList.get(i));
        }
        System.out.println(myList);
        System.out.println(list.size());
        return new  Result(Code.OK,myList,null);
    }

    @PostMapping("/identify")
    public Result identify(@RequestParam("file") MultipartFile ocr) throws Exception {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        byte[] file = null;
        try {
            file = ocr.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "上传图片失败");
        }
        try {
            jsonObject = ocrService.dataHandle(file);
        } catch (Exception e) {
            jsonObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            jsonObject.put("result", false);
            jsonObject.put("message", "图片识别出错");
        }
        List<String> list= ocrService.ocr_accurateGeneral( file );

        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(" ");
        }
        String result = sb.toString().trim();
        result=result.replaceAll( "\\s+","" );
        List<String> myList = new ArrayList<>();
        String[] splitStrings = result.split("、");
        for(String item:splitStrings){
            myList.add( item );
        }

        List<String> listadd = foodaddService.listadd();
        List<String> listsugar = foodaddService.listsugar();
        List<String> listtran = foodaddService.listtran();
        List<String> list2=new ArrayList<>();
        for(String str:listadd){
            if(myList.contains( str )){
                list2.add( str );
//                return "添加剂："+list2.get( 0 );
            }
        }
        for(String str:listsugar){
            if(myList.contains( str )){
                list2.add( str );
//                return "添加糖："+list2.get( 0 );
            }
        }
        for(String str:listtran){
            if(myList.contains( str )){
                list2.add( str );
//                return "反式脂肪酸："+list2.get( 0 );
            }
        }
        if(list2.size()>0||list2!=null){
//            return Result.success("添加剂:"+list2);
            return new Result(Code.OK, "添加剂:"+list2, null);
        }else {
            return new Result(Code.OK,"经识别，不含任何添加剂哟！",null);
        }
    }
}