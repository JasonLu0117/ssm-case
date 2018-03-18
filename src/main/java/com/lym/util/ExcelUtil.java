package com.lym.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 
 * @Description: 读取Excel工具（为本项目的格式定制，非通用）
 *
 */
public class ExcelUtil {
    
    public static List<Map<String, String>> readXls(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        
        File xlsFile = new File(path);
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            // 获得行数
            int rows = sheet.getLastRowNum() + 1;
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null) {
                continue;
            }
            int cols = tmp.getPhysicalNumberOfCells();
            // 读取数据，跳过第一行title
            for (int row = 1; row < rows; row++) {
                Row r = sheet.getRow(row);
                // 当前项目中，只有两列。
                /*
                for (int col = 0; col < 2; col++) {
                    // 有时会有空列，当为空时，跳出循环。
                    if (r.getCell(col) == null) {
                        break;
                    } else {
                        System.out.printf("%10s", r.getCell(col).getStringCellValue());
                    }
                }
                System.out.println();
                */
                // 有时会有空列，当为空时，跳出循环。
                if (r.getCell(0) == null) {
                    break;
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(r.getCell(0).getStringCellValue(), r.getCell(1).getStringCellValue());
                    list.add(map);
                }
            }
        }
        return list;
    }
    
    public static void writeXls(String path) throws IOException {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet("sheet1");

        for (int row = 0; row < 10; row++) {
            HSSFRow rows = sheet.createRow(row);
            for (int col = 0; col < 10; col++) {
                // 向工作表中添加数据
                rows.createCell(col).setCellValue("data" + row + col);
            }
        }

        File xlsFile = new File(path);
        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);
    }
    
    /*
    public static void main(String[] args) {
        try {
            List<Map<String, String>> list = readXls("D:\\model.xlsx");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */          
}
