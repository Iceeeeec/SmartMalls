package com.hsasys.baiduocr.service.impl;


import com.baidu.aip.ocr.AipOcr;
import com.hsasys.baiduocr.service.OcrService;
import com.hsasys.context.BaseContext;
import com.hsasys.dao.domain_mapper.PhysicalMapper;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.service.select.PhysicalService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${baidu.ocr.appId}")
    private String APP_ID="58618208";
    @Value("${baidu.ocr.apiKey}")
    private String API_KEY="l0uRAKrJxWGCzmir4ldxf8YR";
    @Value("${baidu.ocr.secretKey}")
    private String SECRET_KEY="owm3G3IIavSoq5lQAnddifNiMot8c7TO";
    @Autowired
    private PhysicalMapper physicalMapper;
    @Autowired
    private PhysicalService physicalService;


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
    public Map<String, String> ocr_accurateGeneral(byte[] bytes) {
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
//        System.out.println("JSON数组===================>" + jsonArray);
        int length = jsonArray.length();
        List<String> list = new ArrayList<>(length);
        System.out.println("jsonArray length ==============>" + length);
        for (int i = 0; i < length; i++) {
            org.json.JSONObject result = (org.json.JSONObject) jsonArray.get(i);
            list.add(result.getString("words"));
        }
        Map<String, String> structuralization = structuralization(list);
//        System.out.println(list);
        return structuralization;
    }

    /**
     * 处理PDF文件，将PDF页面转换为图片，并执行OCR识别
     */
    @Override
    public Map<String, String> ocrPdf(byte[] file) throws IOException {
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
        Map<String, String> structuralization = structuralization(allText);

        document.close();  // 关闭PDF文件
        return structuralization;
    }


    /**
     * 将PDF每一页转换为图片
     */
    private byte[] pdfPageToImage(PDDocument document, int pageIndex) throws IOException {
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
    private Map<String, String> structuralization(List<String> list) {
        Map<String, String> results = new LinkedHashMap<>();
        Set<String> unitList = physicalMapper.selectAllUnit();
        List<String> itemList = physicalMapper.selectAllItem();
        String[] itemArray = itemList.toArray(new String[0]);

        int i = 0;
        while (i < list.size() - 1) {
            String current = list.get(i);

            // 处理体检项目
            if (Arrays.asList(itemArray).contains(current) && i + 1 < list.size()) {
                String value = list.get(i + 1);
                if (!value.isEmpty() && !"暂无".equals(value) && !unitList.contains(value) && !value.equals("血糖")) {
                    results.put(current, value);
                }
                i=value.equals("血糖")?i+1:i+3;
            } else {
                i++;
            }
        }
        
        return results;
    }

    @Override
    public Map<String, String> dataHandle(byte[] bytes) throws Exception {
        return ocr_accurateGeneral(bytes);
    }

}
