package com.hsasys.utils;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfToImage
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
     * 将单页 PDF 转换为图像并返回 File 对象（不指定输出路径）
     * @param pdfPath PDF 文件路径
     * @return 转换后的图像文件对象
     * @throws IOException 如果转换过程中出现错误
     */
    public static File convertSinglePagePdfToImage(String pdfPath) throws IOException
    {
        // 加载 PDF 文件
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            // 确保 PDF 只有一页
            if (document.getNumberOfPages() != 1) {
                throw new IllegalArgumentException("The PDF should contain exactly one page.");
            }

            // 创建 PDFRenderer 对象
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 渲染 PDF 页面为图像
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300); // 渲染第 0 页

            // 创建临时文件并将图像写入文件
            File tempFile = File.createTempFile("pdf_page", ".png");
            ImageIO.write(bufferedImage, "PNG", tempFile);

            // 确保 JVM 退出时删除临时文件
            tempFile.deleteOnExit();

            // 返回生成的临时文件
            return tempFile;
        }
    }

}
