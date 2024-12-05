package com.hsasys.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfToImage {

    /**
     * 将 PDF 转换为图像
     * @param pdfPath PDF 文件路径
     * @param imagePath 图片保存目录
     * @throws IOException 如果转换过程中出现错误
     */
    public static void convertPdfToImage(String pdfPath, String imagePath) throws IOException {
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

    public static void main(String[] args) {
        try {
            String pdfPath = "C:\\Users\\86183\\Desktop\\123.pdf"; // PDF 文件路径
            String imagePath = "C:\\Users\\86183\\Desktop"; // 图片保存目录
            convertPdfToImage(pdfPath, imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
