package com.github.wxiaoqi.security.common.util;

import com.github.pagehelper.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入导出工具类
 */
public   class ExcelUtils<T> {

    private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel



    /**
     * 读取excel文件更新单表数据，其单元格格式为文本
     * @param in
     * @param fileName
     * @return
     */
    public static<T> List<List<T>>  importExcel(InputStream in,String fileName,T entity) throws Exception {
        List<List<List<String>>> lists0= inToList(in,fileName);
        in.close();//关闭流
        List<List<T>> tLists=new ArrayList<>();
        for(List<List<String>> lists1:lists0){
            List<T> tList=new ArrayList<>();
            for(List<String> list:lists1){
                //集合中必须加入不同对象，否则所有元素会同步
                T t= (T) entity.getClass().newInstance();
                //获取参数类
                Class cls = t.getClass();
                Field[] fields = cls.getDeclaredFields();

                //排除主键
                for(int j = 1;j < fields.length; j++){
                    String value=null;
                    if(list.size()>=fields.length){
                        value=list.get(j-1);
                    }else{
                        if(j<list.size()+1){
                            value=list.get(j-1);

                        }
                    }
                    Field f = fields[j];
                    f.setAccessible(true);
                    try {
                        if(value!=null&&value.trim()!=""){
                            //  System.out.println(value+"--"+convertByType(f.getGenericType().toString(),value));
                            ReflectionUtils.setFieldValue(t,f.getName(),convertByType(f.getGenericType().toString(),value));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.out.println("格式转换异常");
                    }
                }

                tList.add(t);
            }

            tLists.add(tList);
        }
        return  tLists;
    }

    /**
     * 选中导出单表除主键外所有字段生成excel文件，其单元格格式为文本
     * @param request
     * @param response
     * @param excelHeader
     * @param sheetName
     * @param list
     * @throws Exception
     */
    public static<T> void exportExcel(HttpServletRequest request, HttpServletResponse response, String[] excelHeader, String sheetName, List<T> list, String fileName) throws Exception {

        if (list == null || list.size() ==0) {
            return;
        }
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat format=wb.createDataFormat();

        // 设置居中样式
        style.setDataFormat(format.getFormat("@")); //设置单元格格式为文本
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

        // 设置合计样式
        HSSFCellStyle style1 = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        font.setFontHeightInPoints((short)12); //设置字体大小
        style1.setFont(font);
        style1.setDataFormat(format.getFormat("@")); //设置单元格格式为文本
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

        // 设置列宽度（像素）   单元格列宽
        int[] excelHeaderWidth = {150};
        //行数
        int columnNum=excelHeader.length;
        for (int i = 0; i < columnNum; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[0]);
        }

        // 添加表格头
        for (int i = 0; i < columnNum; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style1);
        }

        //循环列表数据，逐个添加
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = list.get(i);
            //获取参数类
            Class cls = t.getClass();
            //将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
            Field[] fields = cls.getDeclaredFields();
            for(int j = 1;j < columnNum+1; j ++){
                Field f = fields[j];
                f.setAccessible(true);
                try {
                    //f.getName()得到对应字段的属性名，f.get(t)得到对应字段属性值,f.getGenericType()得到对应字段的类型
                    // System.out.println("属性名："+f.getName()+"；属性值："+f.get(t)+";字段类型：" + f.getGenericType());
                    HSSFCell cell = row.createCell(j-1);
                    if(f.get(t)==null){
                        cell.setCellValue("");
                    }else{
                        //时间类型格式化
                        if(f.getGenericType().toString().indexOf("Date")!=-1){
                            cell.setCellValue(dateFormat.format(f.get(t)));
                        }else {
                            cell.setCellValue(f.get(t).toString());
                        }

                    }

                    cell.setCellStyle(style);
                } catch (IllegalArgumentException | IllegalAccessException e) {

                    e.printStackTrace();
                }
            }

        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        //注意此处文件名称如果想使用中文的话，要转码new String( "中文".getBytes( "gb2312" ), "ISO8859-1" )
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") +
                        UUIDUtils.generateShortUuid() + ".xls");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 字符串类型转换
     * @param type
     * @param value
     * @return
     * @throws ParseException
     */
    public static Object convertByType(String type,String value) throws ParseException {
        if(!value.trim().equals("")&&value!=null){
            if(type.indexOf("Date")!=-1){
                return  dateFormat.parse(value);
            }
            if(type.indexOf("Integer")!=-1){
                return  Integer.valueOf(value);
            }
            if(type.indexOf("BigDecimal")!=-1){
                return new BigDecimal(value).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if(type.indexOf("Short")!=-1){
                return Short.valueOf(value);
            }
            if(type.indexOf("Byte")!=-1){
                return Byte.valueOf(value);
            }
            if(type.indexOf("Long")!=-1){
                return Long.valueOf(value);
            }
            if(type.indexOf("Float")!=-1){
                return 	Float.valueOf(value);
            }
            if(type.indexOf("Double")!=-1){
                return Double.valueOf(value);
            }
            if(type.indexOf("Boolean")!=-1){
                return  Boolean.valueOf(value);
            }
            return  value;
        }
        return null;
    }
    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param in,fileName
     * @return
     * @throws Exception
     */
    public static   List<List<List<String>>> inToList(InputStream in, String fileName) throws Exception{
        List<List<List<String>>> lists = null;
        ExcelUtils utils=new ExcelUtils();
        //创建Excel工作薄
        Workbook work = utils.getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        lists = new ArrayList<>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            List<List<String>> list=new ArrayList<>();
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}
            int firstRowNum=sheet.getFirstRowNum();
            //遍历当前sheet中的所有行
            for (int j = firstRowNum+1; j <=sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(isAllRowEmpty(row,sheet.getRow(firstRowNum))){continue;}

                //遍历所有的列
                List<String> li = new ArrayList<>();
                for (int k = row.getFirstCellNum(); k <row.getLastCellNum(); k++) {
                    cell = row.getCell(k);
                    li.add(utils.getCellValue(cell));
                }
                list.add(li);
            }
            lists.add(list);
        }
        return lists;

    }

    /**
     * 判断excel某一行是否全部为空或者空格，是则返回true
     * @param row
     * @param firstRow
     * @return
     */
    public static boolean isAllRowEmpty(Row row,Row firstRow) {
        int count = 0;
        //单元格数量
        int rowCount = firstRow.getLastCellNum() - firstRow.getFirstCellNum();
        //判断多少个单元格为空
        for (int c = 0; c < rowCount; c++) {
            Cell cell = row.getCell(c);
            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK || StringUtil.isEmpty((cell+"").trim())){
                count += 1;
            }
        }

        if (count == rowCount) {
            return true;
        }

        return  false;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public  String getCellValue(Cell cell){
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String value = null;
        DecimalFormat df = new DecimalFormat("0"); //数字格式化
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:

                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());

                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = dateFormat.format(cell.getDateCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }


}
