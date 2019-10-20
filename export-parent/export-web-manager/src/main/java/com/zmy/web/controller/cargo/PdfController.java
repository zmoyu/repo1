package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmy.domain.cargo.Export;
import com.zmy.domain.cargo.ExportProduct;
import com.zmy.domain.cargo.ExportProductExample;
import com.zmy.domain.system.User;
import com.zmy.service.cargo.ExportProductService;
import com.zmy.service.cargo.ExportService;
import com.zmy.web.controller.BaseController;
import com.zmy.web.utils.BeanMapUtils;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("cargo/export")
public class PdfController extends BaseController {
    /**
     * 1.入门案例，展示pdf
     * /cargo/export/exportPdf.do?id=${o.id}
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test02.jasper");
        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), new JREmptyDataSource());

        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }*/

    /**
     * 2. 数据填充 - 通过java代码往jasper模板参数设置值
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test03-parameter.jasper");

        Map<String,Object> map = new HashMap<>();
        map.put("username","zmy");
        map.put("email","zmy@qq.com");
        map.put("company","地狱无门自来股份有限公司");
        map.put("deptName","魔芋部");

        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, map, new JREmptyDataSource());
        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }*/

    /**
     * 3. 数据填充 - 通过JDBC数据源填充模板数据
     */
    @Autowired
    private DataSource dataSource;

    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test04-jdbc.jasper");

        //2. 创建JasperPrint对象
        //获取连接对象
        Connection connection = dataSource.getConnection();

        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), connection);
        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

    }*/

    /**
     * 4. 数据填充 - JavaBean数据源填充模板数据
     * @throws Exception
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test05-javabean.jasper");

        //2. 创建JasperPrint对象
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserName("name"+i);
            user.setEmail(i+"@qq.com");
            user.setCompanyName("企业"+i);
            user.setDeptName("部门"+i);
            list.add(user);
        }
        //将list构造为jrdatasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), dataSource);
        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

    }*/


    /**
     * 5. 数据填充 - 分组报表
     *
     * @throws Exception
     */
   /* @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test06-group.jasper");

        //2. 创建JasperPrint对象
        ArrayList<User> list = new ArrayList<>();
        for(int i=0;i<2;i++) {
            for(int j=0;j<5;j++) {
                User user = new User();
                user.setUserName("name"+j);
                user.setEmail(j+"@qq.com");
                user.setCompanyName("企业"+i);
                user.setDeptName("部门"+j);
                list.add(user);
            }
        }
        //将list构造为jrdatasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), dataSource);
        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

    }*/

   /* *//**
     * 6. 数据填充 - 图形报表
     *
     * @throws Exception
     *//*
    @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test07-char.jasper");

        //2. 创建JasperPrint对象
        List list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map map = new HashMap();
            map.put("title", "标题" + i);
            map.put("value", new Random().nextInt(100));
            list.add(map);
        }
        //将list构造为jrdatasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), dataSource);
        //以pdf形势输出
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

    }*/


    /**
     * 7. 7. PDF导出报运单
     *
     * @throws Exception
     */
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    @RequestMapping("/exportPdf")
    public void exportPdf(String id) throws Exception {
        //1.加载jasper文件，获取文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/export.jasper");

        //2. 创建JasperPrint对象
        //查询报运单数据
        //构造模板数据
        //a.根据id查询报运单，将对象转化为map集合
        Export export = exportService.findById(id);
        Map<String ,Object> map = BeanMapUtils.beanToMap(export);
        //查询报运商品列表
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(exportProductExample);


        //将list构造为jrdatasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(exportProducts);
        //2.创建jasperPrint对象
        //参数一：模板文件输出流；参数二：传递到模板文件中的key—value类型的参数；
        // 参数三：数据列表参数
        JasperPrint jasperPrint = JasperFillManager.fillReport(in, map, dataSource);

        //3.以pdf形势输出
        response.setCharacterEncoding("UTF-8");
        //设置浏览器下载响应头
        response.setHeader("content-disposition","attachment;fileName=export.pdf");
       // JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        ServletOutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,out);
        //关流
        out.close();

    }
}
