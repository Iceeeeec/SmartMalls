package com.hsasys.baiduocr.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import com.hsasys.baiduocr.service.OcrService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${baidu.ocr.appId}")
    private String APP_ID="58618208";
    @Value("${baidu.ocr.apiKey}")
    private String API_KEY="l0uRAKrJxWGCzmir4ldxf8YR";
    @Value("${baidu.ocr.secretKey}")
    private String SECRET_KEY="owm3G3IIavSoq5lQAnddifNiMot8c7TO";


    private Pattern hAsDigitPattern = Pattern.compile(".*\\d+.*");
    private Pattern isDigitPattern = Pattern.compile("[0-9]{1,}");
    private Pattern isDigitPrePattern = Pattern.compile("^(\\d+)(.*)");
    private Pattern getStringOfNumbersPattern = Pattern.compile("\\d+");
    private Pattern splitNotNumberPattern = Pattern.compile("\\D+");
    private Pattern floatsNumberPattern = Pattern.compile("[\\d.]{1,}");
    private Pattern trimNumberPattern = Pattern.compile("\\s*|\\t|\\r|\\n");

    /**
     * 百度ocr（高精度包含位置类型 accurateGeneral ）
     *
     * @param bytes
     * @return
     */
    @Override
    public List<String> ocr_accurateGeneral(byte[] bytes) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> options = new HashMap<String, String>();

        //参数（高精度含位置）
        options.put("recognize_granularity", "big");
        options.put("detect_direction", "true");
        options.put("vertexes_location", "true");
        options.put("probability", "true");

        //accurateGeneral（高精度含位置）
        org.json.JSONObject res = client.accurateGeneral(bytes, options);

        JSONArray jsonArray = res.getJSONArray("words_result");
        //输出JSON数组
        System.out.println("JSON数组===================>" + jsonArray);
        int length = jsonArray.length();
        List<String> list = new ArrayList<>(length);
        System.out.println("jsonArray length ==============>" + length);
        for (int i = 0; i < length; i++) {
            org.json.JSONObject result = (org.json.JSONObject) jsonArray.get(i);
            list.add(result.getString("words"));
        }
//        System.out.println(list);
        return list;
    }



    @Override
    public JSONObject dataHandle(byte[] bytes) throws Exception {
        List<String> list = ocr_accurateGeneral(bytes);
//        return structuralization(list);
        return null;
    }

}
