package com.hsasys.service.ocr.impl;


import com.baidu.aip.ocr.AipOcr;
import com.hsasys.service.ocr.OcrService;
import com.hsasys.constant.AppHttpCodeEnum;
import com.hsasys.mapper.PhysicalMapper;
import com.hsasys.domain.vo.OcrResultVo;
import com.hsasys.result.Result;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${baidu.ocr.appId}")
    private String APP_ID;
    @Value("${baidu.ocr.apiKey}")
    private String API_KEY;
    @Value("${baidu.ocr.secretKey}")
    private String SECRET_KEY;
    @Autowired
    private PhysicalMapper physicalMapper;

    /**
     * 百度ocr（高精度包含位置类型 accurateGeneral ）
     *
     * @param bytes
     * @return
     */
    @Override
    public List<OcrResultVo> ocr_accurateGeneral(byte[] bytes)
    {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> options = new HashMap<>();

        //参数（高精度含位置）
        options.put("recognize_granularity", "big");
        options.put("detect_direction", "true");
        options.put("vertexes_location", "true");
        options.put("probability", "true");

        //accurateGeneral（高精度含位置）
        org.json.JSONObject res = client.accurateGeneral(bytes, options);

        JSONArray jsonArray = res.getJSONArray("words_result");

        int length = jsonArray.length();
        List<String> list = new ArrayList<>(length);
        System.out.println("jsonArray length ==============>" + length);
        for (int i = 0; i < length; i++) {
            org.json.JSONObject result = (org.json.JSONObject) jsonArray.get(i);
            list.add(result.getString("words"));
        }
        List<OcrResultVo> ocrResultVos = handleList(list);

        return ocrResultVos;
    }

    /**
     * 处理PDF文件，将PDF页面转换为图片，并执行OCR识别
     */
    @Override
    public List<OcrResultVo> ocrPdf(byte[] file) throws IOException {
        PDDocument document = PDDocument.load(file);  // 使用byte[]加载PDF文件
        int pageCount = document.getNumberOfPages();
        List<String> allText = new ArrayList<>();

        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        for (int i = 0; i < pageCount; i++) {
            byte[] imageBytes = pdfPageToImage(document, i);  // 将PDF页面转换为图片
            HashMap<String, String> options = new HashMap<>();
            options.put("recognize_granularity", "big");
            options.put("detect_direction", "true");
            options.put("vertexes_location", "true");
            options.put("probability", "true");

            org.json.JSONObject res = client.accurateGeneral(imageBytes, options);
            JSONArray wordsResult = res.getJSONArray("words_result");

            // 提取OCR结果
            for (int j = 0; j < wordsResult.length(); j++) {
                org.json.JSONObject wordResult = wordsResult.getJSONObject(j);
                allText.add(wordResult.getString("words"));
            }
        }
        List<OcrResultVo> ocrResultVos = handleList(allText);

        document.close();  // 关闭PDF文件
        return ocrResultVos;
    }

    @Override
    public Result handleOcr(MultipartFile ocr)
    {
        byte[] file = null;
        try {
            file = ocr.getBytes();
        } catch (IOException e) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件读取失败");
        }
        //处理ocr
        try {
            String fileName = ocr.getOriginalFilename();
            assert fileName != null;
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 如果是PDF文件，进行PDF OCR处理
            List<OcrResultVo> result = null;
            if (fileExtension.equals("pdf"))
            {
                result = ocrPdf(file);
                return Result.success(result);
            }
            // 如果是图片，进行图片OCR处理
            else
            {
                result = ocr_accurateGeneral(file);
                return Result.success(result);
            }
        } catch (IOException e) {
            return Result.error(AppHttpCodeEnum.IDENTIFICATION_FAILED.getCode(), AppHttpCodeEnum.IDENTIFICATION_FAILED.getMsg());
        }
    }


    /**
     * 将PDF每一页转换为图片
     */
    private byte[] pdfPageToImage(PDDocument document, int pageIndex) throws IOException
    {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 高质量图片
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将OCR识别结果转换为简单的键值对形式
     * @param list OCR识别的文本列表
     * @return 检查结果的Map
     */
    private List<OcrResultVo> handleList(List<String> list)
    {
        Map<String, String> results = new LinkedHashMap<>();
        List<String> itemList = physicalMapper.selectAllItem();

        List<OcrResultVo> result = new ArrayList<>();
        int i = 0;
        while (i < list.size() - 1)
        {
            String current = list.get(i);
            //查询单位
            String unit = physicalMapper.selectUnitByItemName(current);
            // 处理体检项目
            if (itemList.contains(current) && i + 1 < list.size())
            {
                String value = list.get(i + 1);
                //如果后面是数字则加进来
                if (!value.isEmpty() && NumberUtils.isCreatable(value))
                {
                    //封装返回的对象
                    OcrResultVo ocrResultVo = OcrResultVo.builder()
                            .itemName(current)
                            .content(value)
                            .unit(unit).build();
                    result.add(ocrResultVo);
                }

                i = value.equals(current) ? i + 1 : i + 3;
            }
            else
            {
                i++;
            }
        }
        return result;
    }

}
