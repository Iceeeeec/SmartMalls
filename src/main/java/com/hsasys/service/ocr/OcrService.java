package com.hsasys.service.ocr;


import com.hsasys.domain.vo.OcrResultVo;
import com.hsasys.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * OcrService接口
 * @author gaohongtao
 * @date 2022/10/20.
 */
public interface OcrService {

    /**
     * 数据处理（ocr 高精度文字识别）
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    List<OcrResultVo> ocr_accurateGeneral(byte[] bytes)throws Exception;

    /**
     * 处理PDF文件，转换为图片并识别其中的文字
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    List<OcrResultVo> ocrPdf(byte[] bytes) throws IOException;

    Result handleOcr(MultipartFile ocr);
}