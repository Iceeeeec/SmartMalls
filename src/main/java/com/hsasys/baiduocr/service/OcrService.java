package com.hsasys.baiduocr.service;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * OcrService接口
 * @author gaohongtao
 * @date 2022/10/20.
 */
public interface OcrService {

    /**
     * 数据处理（Ocr化和数据结构化）
     * @param bytes 字节数组
     * @return 格式化后的json数据
     * @throws Exception
     */
    public JSONObject dataHandle(byte[] bytes) throws Exception;

    /**
     * 数据处理（ocr 印章识别）
     * @param url
     * @return
     * @throws Exception
     */
//    public JSONObject ocr_sear(String url) throws IOException;

    /**
     * 数据处理（ocr 高精度文字识别）
     * @param bytes
     * @return
     * @throws Exception
     */
    public List<String> ocr_accurateGeneral(byte[] bytes)throws Exception;
}