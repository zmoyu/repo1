package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmy.service.cargo.ContractProductService;
import com.zmy.vo.ContractProductVo;
import com.zmy.web.controller.BaseController;
import com.zmy.web.utils.DownloadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    /**
     * //进入出货表页面
     *
     * @return
     */
    @RequestMapping("/print")
    public String print() {

        return "cargo/print/contract-print";
    }

    /**
     * /cargo/contract/printExcel.do
     * //导出出货表,XSSFWorkbook普通导出
     */
   /* @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //创建工作簿,工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("出货表");
        //参考模板，设置每列列宽
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 26 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 8 * 256);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        //创建第一行
        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);
        Cell cell = row.createCell(1);
        cell.setCellStyle(this.bigTitle(workbook));
        //指定第一行的内容：2018-12---> 2018年12月份出货表
        String value = inputDate.replace("-0", "-").replace("-", "年") + "月份出货表";
        cell.setCellValue(value);

        //创建第二行
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        String titles[] = new String[]{"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i + 1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.title(workbook));
        }

        //调用service导出数据行
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list != null && list.size() > 0) {
            int index = 2;
            for (ContractProductVo cpv : list) {
                row = sheet.createRow(index++);
                row.setHeightInPoints(24);
                cell = row.createCell(1);
                cell.setCellValue(cpv.getCustomName());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(2);
                cell.setCellValue(cpv.getContractNo());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(3);
                cell.setCellValue(cpv.getProductNo());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(4);
                cell.setCellValue(cpv.getCnumber());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(5);
                cell.setCellValue(cpv.getFactoryName());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(6);
                cell.setCellValue(cpv.getDeliveryPeriod());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(7);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpv.getShipTime()));
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(8);
                cell.setCellValue(cpv.getTradeTerms());
                cell.setCellStyle(this.text(workbook));
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        new DownloadUtil().download(bos,response,"出货表.xlsx");
    }*/

    /**
     * 模板导出1
     *
     * @return
     */
    /*@RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //创建工作簿，工作表
        InputStream in = session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sheet = workbook.getSheetAt(0);
        //创建第一行
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);
        //设置第一行标题
        String value = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
        cell.setCellValue(value);


        //创建第二行
        row = sheet.getRow(2);
        CellStyle cellStyles[] = new CellStyle[8];
        for (int i = 0; i < cellStyles.length; i++) {
            cellStyles[i] = row.getCell(i + 1).getCellStyle();
        }

        //调用service，导出数据行
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        int index = 2;
        if (list != null && list.size() > 0) {
            for (ContractProductVo cpv : list) {
                row = sheet.createRow(index++);
                cell = row.createCell(1);
                cell.setCellValue(cpv.getCustomName());
                cell.setCellStyle(cellStyles[0]);

                cell = row.createCell(2);
                cell.setCellValue(cpv.getContractNo());
                cell.setCellStyle(cellStyles[1]);

                cell = row.createCell(3);
                cell.setCellValue(cpv.getProductNo());
                cell.setCellStyle(cellStyles[2]);

                cell = row.createCell(4);
                cell.setCellValue(cpv.getCnumber());
                cell.setCellStyle(cellStyles[3]);

                cell = row.createCell(5);
                cell.setCellValue(cpv.getFactoryName());
                cell.setCellStyle(cellStyles[4]);

                cell = row.createCell(6);
                cell.setCellValue(cpv.getDeliveryPeriod());
                cell.setCellStyle(cellStyles[5]);

                cell = row.createCell(7);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpv.getShipTime()));
                cell.setCellStyle(cellStyles[6]);

                cell = row.createCell(8);
                cell.setCellValue(cpv.getTradeTerms());
                cell.setCellStyle(cellStyles[7]);
            }
        }
        //导出下载
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        new DownloadUtil().download(bos,response,"出货表.xlsx");
    }*/

    /**
     * 出货表导出（3）SXSSF导出，避免内存溢出，支持百万数据导出
     * 注意：
     * 1.不支持模板导出
     * 2.SXSSF样式不能超过6万4
     *
     * @param inputDate
     * @throws Exception
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //创建工作簿,工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("出货表");
        //参考模板，设置每列列宽
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 26 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 8 * 256);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        //创建第一行
        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);
        Cell cell = row.createCell(1);
        cell.setCellStyle(this.bigTitle(workbook));
        //指定第一行的内容：2018-12---> 2018年12月份出货表
        String value = inputDate.replace("-0", "-").replace("-", "年") + "月份出货表";
        cell.setCellValue(value);

        //创建第二行
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        String titles[] = new String[]{"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i + 1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.title(workbook));
        }

        //调用service导出数据行
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list != null && list.size() > 0) {
            int index = 2;
            for (ContractProductVo cpv : list) {
                for (int i = 0; i < 1000; i++) {
                    row = sheet.createRow(index++);
                    row.setHeightInPoints(24);
                    cell = row.createCell(1);
                    cell.setCellValue(cpv.getCustomName());

                    cell = row.createCell(2);
                    cell.setCellValue(cpv.getContractNo());

                    cell = row.createCell(3);
                    cell.setCellValue(cpv.getProductNo());

                    cell = row.createCell(4);
                    cell.setCellValue(cpv.getCnumber());

                    cell = row.createCell(5);
                    cell.setCellValue(cpv.getFactoryName());

                    cell = row.createCell(6);
                    cell.setCellValue(cpv.getDeliveryPeriod());

                    cell = row.createCell(7);
                    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpv.getShipTime()));

                    cell = row.createCell(8);
                    cell.setCellValue(cpv.getTradeTerms());

                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        new DownloadUtil().download(bos, response, "出货表.xlsx");
    }


    //大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }
}
