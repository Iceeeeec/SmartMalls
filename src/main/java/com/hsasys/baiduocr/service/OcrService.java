package com.hsasys.baiduocr.service;


import java.io.IOException;
import java.util.Map;

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
    Map<String, String> dataHandle(byte[] bytes) throws Exception;

    /**
     * 数据处理（ocr 印章识别）
     * @param url
     * @return
     * @throws Exception
     */
//    public JSONObject ocr_sear(String url) throws IOException;

    /**
     * 数据处理（ocr 高精度文字识别）
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public Map<String, String> ocr_accurateGeneral(byte[] bytes)throws Exception;

    /**
     * 处理PDF文件，转换为图片并识别其中的文字
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public Map<String, String> ocrPdf(byte[] bytes) throws IOException;
}