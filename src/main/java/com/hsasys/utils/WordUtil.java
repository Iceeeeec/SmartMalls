package com.hsasys.utils;

import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WordUtil {

    /**
     * 根据模板生成新Word文档内容（字节数组）
     *
     * @param inputUrl 模板文件路径
     * @param textMap  文本替换的键值对集合
     * @return 返回生成的Word文档内容字节数组
     * @throws IOException 读取模板或生成文件失败时抛出
     */
    public static byte[] generateWordBytes(String inputUrl, Map<String, String> textMap) throws IOException {
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(inputUrl));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 替换段落中的占位符
            replaceTextInParagraphs(document.getParagraphs(), textMap);

            // 替换表格中的占位符
            replaceTextInTables(document.getTables(), textMap);

            // 将内容写入字节输出流
            document.write(outputStream);

            // 返回字节数组
            return outputStream.toByteArray();
        }
    }

    /**
     * 替换段落中的占位符
     *
     * @param paragraphs 段落列表
     * @param textMap    文本替换的键值对集合
     */
    private static void replaceTextInParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> textMap) {
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            if (containsPlaceholder(text)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String updatedText = replacePlaceholders(run.toString(), textMap);
                    run.setText(updatedText, 0);
                }
            }
        }
    }

    /**
     * 替换表格中的占位符
     *
     * @param tables 表格列表
     * @param textMap 文本替换的键值对集合
     */
    private static void replaceTextInTables(List<XWPFTable> tables, Map<String, String> textMap) {
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        if (containsPlaceholder(paragraph.getText())) {
                            for (XWPFRun run : paragraph.getRuns()) {
                                String updatedText = replacePlaceholders(run.toString(), textMap);
                                run.setText(updatedText, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断文本中是否包含占位符
     *
     * @param text 要判断的文本
     * @return 包含占位符返回 true，否则返回 false
     */
    private static boolean containsPlaceholder(String text) {
        return text != null && text.contains("${");
    }

    /**
     * 根据键值对替换占位符
     *
     * @param text    包含占位符的文本
     * @param textMap 文本替换的键值对集合
     * @return 替换后的文本
     */
    private static String replacePlaceholders(String text, Map<String, String> textMap) {
        for (Map.Entry<String, String> entry : textMap.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (text.contains(placeholder)) {
                text = text.replace(placeholder, entry.getValue());
            }
        }
        return text;
    }
}