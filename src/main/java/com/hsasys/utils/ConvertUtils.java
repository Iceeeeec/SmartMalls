package com.hsasys.utils;

import com.spire.doc.FileFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class ConvertUtils
{
    /**
     * 将 PDF 转换为图像
     * @param pdfPath PDF 文件路径
     * @param imagePath 图片保存目录
     * @throws IOException 如果转换过程中出现错误
     */
    public static void convertPdfToImage(String pdfPath, String imagePath) throws IOException
    {
        // 加载 PDF 文件
        PDDocument document = PDDocument.load(new File(pdfPath));

        // 获取 PDF 中的页面数量
        int numberOfPages = document.getNumberOfPages();

        // 遍历所有页面
        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            PDPage page = document.getPage(pageIndex);

            // 创建 PDFRenderer 对象
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 将当前页面渲染为图像
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 300 DPI 提供较高的图像质量

            // 将 BufferedImage 保存为文件
            File outputImageFile = new File(imagePath + "/page_" + (pageIndex + 1) + ".png");
            ImageIO.write(bufferedImage, "PNG", outputImageFile);

            System.out.println("Page " + (pageIndex + 1) + " converted to image successfully.");
        }

        // 关闭 PDF 文档
        document.close();
    }


    /**
     * 将单页 PDF 转换为图像并返回字节数组
     *
     * @param pdfBytes PDF 文件的字节数组
     * @return 转换后的图像字节数组
     * @throws IOException 如果转换过程中出现错误
     */
    public static byte[] convertSinglePagePdfToImage(byte[] pdfBytes) throws IOException {
        // 使用输入流加载 PDF 文档
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 创建 PDFRenderer 对象
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 渲染 PDF 页面为图像
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300); // 渲染第 0 页

            // 将图像写入输出流
            ImageIO.write(bufferedImage, "PNG", outputStream);

            // 返回图像字节数组
            return outputStream.toByteArray();
        }
    }
    /**
     * word文档转换为pdf
     * @param file
     * @return
     */
    public static byte[] wordToPdf(byte[] file)
    {
        //实例化Document类的对象
        com.spire.doc.Document doc = new com.spire.doc.Document();
        try(InputStream in = new ByteArrayInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            doc.loadFromStream(in, FileFormat.Docx);
            // 获取字体文件目录
            InputStream fontInStream = ConvertUtils.class.getClassLoader().getResourceAsStream("font/simsun.ttc");
            if(fontInStream == null)
            {
                throw new RuntimeException("字体文件缺失，请稍后重试！");
            }
            // 将单个 InputStream 包装成 InputStream 数组
            InputStream[] customFonts = new InputStream[]{fontInStream};
            // 设置自定义字体目录
            doc.setCustomFonts(customFonts);
            //保存为PDF格式
            doc.saveToStream(outputStream, FileFormat.PDF);
            // 返回生成的PDF字节数组
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
