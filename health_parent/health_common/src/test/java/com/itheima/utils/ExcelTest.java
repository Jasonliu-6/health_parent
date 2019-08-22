package com.itheima.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelTest {

   /* @Test*/
    public void excelReadTest() throws IOException {
        //1.创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\fzoo\\Desktop\\CZJK-78\\第5章\\资源\\poi测试数据.xlsx");

        //2.获取工作表 工作簿中有多个工作表
        XSSFSheet sheet = workbook.getSheetAt(0);

        //3.遍历工作表的数据行
        for (Row row:sheet){

            //4.遍历每一行中的 单元格
            for (Cell cell:row){
                //5.获取每一个单元格中的数据
                System.out.println(cell.getStringCellValue());
            }
        }
    }

    /*@Test*/
    public void excelRead2Test() throws IOException {
        //1.创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\fzoo\\Desktop\\CZJK-78\\第5章\\资源\\poi测试数据.xlsx");

        //2.获取工作表 工作簿中有多个工作表
        XSSFSheet sheet = workbook.getSheetAt(0);

        //3.获取工作表最后一行数据的索引
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <=lastRowNum; i++) {

            //4.根据行号 获取行
            XSSFRow row = sheet.getRow(i);

            //根据单元格号 获取单元格
            short lastCellNum = row.getLastCellNum();

            for(short j=0;j<lastCellNum;j++){
                XSSFCell cell = row.getCell(j);

                System.out.println(cell.getStringCellValue());
            }

        }

        workbook.close();

    }

    /*@Test*/
    public void excelWrite() throws IOException {
        //1.创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();

        //2.创建sheet 指定sheet的名字
        XSSFSheet sheet = workbook.createSheet("itcast");

        //2 创建工作表行 0行 表示第一行
        Row row0 =  sheet.createRow(0);

        row0.createCell(0).setCellValue("小红");
        row0.createCell(1).setCellValue(12);
        row0.createCell(2).setCellValue("北京");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("小猪");
        row1.createCell(1).setCellValue(12);
        row1.createCell(2).setCellValue("上海");

        //3.创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\fzoo\\Desktop\\CZJK-78\\第5章\\资源\\3.xlsx");

        //4.将表格数据写入文件输出流中
        workbook.write(fileOutputStream);

        //5.将生成的表格文件 写到磁盘
        fileOutputStream.flush();
        fileOutputStream.close();

        workbook.close();
    }
}