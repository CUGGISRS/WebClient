package com.github.wxiaoqi.security.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ace on 2017/9/10.
 */
public class StringHelper {
    private static int ran=100;

    private static SimpleDateFormat dateFormatYMd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormatYMdHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 将Date对象转化为yyyy-MM-dd格式的时间字符串
     */
    public static String dateYMd2String(Date date) {
        if (date != null) {
            return dateFormatYMd.format(date);
        }
        return null;
    }

    /**
     * 将Date对象转化为某种格式的时间字符串
     */
    public static String dateYMdHMS2String(Date date) {
        if (date != null) {
            return dateFormatYMdHMS.format(date);
        }
        return null;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间字符串转为Date对象
     */
    public static Date stringYMdHMS2Date(String str) {
        if (str != null) {
            try {
                return dateFormatYMdHMS.parse(str);
            } catch (ParseException e) {
                System.out.println("时间字符串格式不正确---" + e.getMessage());
            }
        }
        return null;
    }

    public static  String getUniqueCode(int ran){
        long now = System.currentTimeMillis();
        //获取4位年份数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取时间戳
        String time = dateFormat.format(now);
        //获取三位随机数    要是一段时间内的数据连过大会有重复的情况，所以再套一层方法
    //    int ran=(int) ((Math.random()*9+1)*100);
        return time + ran;
    }

    /**
     * 获得20位随机唯一的数字组成的字符串
     */
    public  static String getUniqueCode(){
       String number= getUniqueCode(ran);
        ran++;
        if(ran>999){
            ran=100;
        }
        return number;
    }


    /**
     * 返回对象的字符串表示形式 ，如果对象为null则返回空字符串
     * @param obj
     * @return
     */
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }

    /**
     * 判断是否为 整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {

        if(isNullOrEmpty(str)){
            return false;
        }
        Pattern p = Pattern.compile("^[-\\+]?[\\d]*$");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }


    /** 判断一个字符串是否含有数字
     * @param content
     * @return
     */
    public static boolean containDigit(String content) {
        if(isNullOrEmpty(content)){
            return false;
        }
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为null或空字符串，是则返回true
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str){
        return  str==null||str.length()==0;
    }


    /**
     * 解决url中编码问题
     */
    public static String string2URLEncoder(String str) {
        if(str==null){
            return null;
        }
        try{
            //获取的语言环境的编码
            Charset charset = Charset.defaultCharset();
            str = new String(str.getBytes(), charset);
            str=URLEncoder.encode(str, "utf-8").replace("+","%20");
            return str;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
       return null;
    }
}
