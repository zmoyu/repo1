package com.zmy.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;


import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 编写测试类，实现导出excel、导入excel
 */
public class App {

    //导出excel
    @Test
    public void write() throws Exception {
        //创建一个工作簿
        //Workbook workbook = new HSSFWorkbook();//处理excel2003版本，后缀.xls
        Workbook wb = new XSSFWorkbook();//处理excel2007版本，后缀xlsx

        //创建一个工作表
        Sheet sheet = wb.createSheet("测试");

        //创建第二行
        Row row = sheet.createRow(1);
        //创建第二列
        Cell cell = row.createCell(1);
        //设置单元格内容
        cell.setCellValue("不良人");

        //将excel保存到磁盘当中
        wb.write(new FileOutputStream("E:/text.xlsx"));
        wb.close();
    }

    @Test
    public void read() throws Exception {
        //根据excel文件加载工作簿
        Workbook workbook = new XSSFWorkbook("E:\\text.xlsx");

        //读取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        //d读取第二行
        Row row = sheet.getRow(1);
        //读取单元格
        Cell cell = row.getCell(1);
        //获取单元格内容
        String value = cell.getStringCellValue();
        System.out.println("第二行第二列 = " + value);

        //获取有效行列
        System.out.println("总记录行 = " + sheet.getPhysicalNumberOfRows());
        System.out.println("总记录列 = " + row.getPhysicalNumberOfCells());

        workbook.close();
    }
}
