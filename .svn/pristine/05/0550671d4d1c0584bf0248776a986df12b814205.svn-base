package com.zd.earth.manage.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
    private static int ran = 100;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat ymFormat = new SimpleDateFormat("yyyy-MM");
    private static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

    private static SimpleDateFormat yyyyMMddCFormat = new SimpleDateFormat("yyyy年MM月dd日");



    /**
     * 将Date对象转化为 yyyy年MM月dd日 格式的时间字符串
     */
    public static String date2yyyyMMddC(Date datetime) {
        if (datetime != null) {
            return yyyyMMddCFormat.format(datetime);
        }
        return null;
    }


    /**
     * 获得带中文url转码后的url
     */
    public static String getEncodeUrl(String url) throws UnsupportedEncodingException {
        if (isNullOrEmpty(url)) {
            return null;
        }
        int index = url.indexOf("/file/");
        if (index == -1) {
            return null;
        }
        index += 6;
        String b = url.substring(0, index);
        String a = url.substring(index);
        String[] as = a.split("/");
        StringBuffer sf = new StringBuffer(b);
        for (int i = 0, len = as.length; i < len; i++) {
            String item = as[i];
            sf.append(string2URLEncoder(item));
            sf.append("/");
        }
        return sf.substring(0, sf.length() - 1);
    }

    public static String string2URLEncoder(String str) throws UnsupportedEncodingException {
        if(str==null){
            return null;
        }
        //获取的语言环境的编码
        Charset charset = Charset.defaultCharset();
        str = new String(str.getBytes(), charset);
        str=URLEncoder.encode(str, "utf-8").replace("+","%20");
        return str;
    }

    /**
     * 解析身份证获得本人性别位数
     */
    public static Integer getSexNumByIdNUmber(String idNUmber) {
        if (idNUmber != null && idNUmber.length() == 18) {
            Integer sexNumber = Integer.valueOf(idNUmber.substring(16, 17));
           return sexNumber;
        }
        return null;
    }
    /**
     * 解析身份证获得本人年龄
     */
    public static Integer getAgeByIdNUmber(String idNUmber) {
        if (idNUmber != null && idNUmber.length() == 18) {
            Date birthday = yyyyMMdd2Date(idNUmber.substring(6, 14));
            Integer age = MyObjectUtil.getAge(birthday);
            return age;
        }
        return null;
    }



    /**
     * 通过性别获得性别位数
     */
    public static Integer getSexNumBySex(String sex) {
        if ("男".equals(sex)) {
            return 1;
        } else if ("女".equals(sex)) {
            return 2;
        }
        return null;
    }


    /**
     * 将yyyyMMdd格式的时间字符串转为Date对象
     *
     * @param yyyyMMdd
     * @return
     */
    public static Date yyyyMMdd2Date(String yyyyMMdd) {
        if (yyyyMMdd != null) {
            try {
                return yyyyMMddFormat.parse(yyyyMMdd);
            } catch (ParseException e) {
                System.out.println("时间字符串格式不正确---" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 将Date对象转化为 yyyyMMdd 格式的时间字符串
     *
     * @param datetime
     * @return
     */
    public static String date2yyyyMMdd(Date datetime) {
        if (datetime != null) {
            return yyyyMMddFormat.format(datetime);
        }
        return null;
    }


    /**
     * 获得时间唯一码
     *
     * @param ran
     * @return
     */
    public static String getUniqueCode(int ran) {
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
     * 将字符串转化为BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal string2BigDecimal(String str) {
        if (str != null) {
            try {
                return new BigDecimal(str);
            } catch (NumberFormatException e) {
                System.out.println("数字字符串格式不正确---" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 将BigDecimal转化为字符串
     *
     * @param bigDecimal
     * @return
     */
    public static String bigDecimal2String(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.toString();
    }

    /**
     * 将yyyy-MM格式的时间字符串转为Date对象
     */
    public static Date ymString2Date(String ym) {
        if (ym != null) {
            try {
                return ymFormat.parse(ym);
            } catch (ParseException e) {
                System.out.println("时间字符串格式不正确---" + e.getMessage());
            }

        }
        return null;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间字符串转为Date对象
     *
     * @param datetime
     * @return
     */
    public static Date string2Date(String datetime) {
        if (datetime != null) {
            try {
                return dateFormat.parse(datetime);
            } catch (ParseException e) {
                System.out.println("时间字符串格式不正确---" + e.getMessage());
            }

        }
        return null;
    }

    /**
     * 将Date对象转化为yyyy-MM-dd HH:mm:ss格式的时间字符串
     *
     * @param datetime
     * @return
     */
    public static String date2String(Date datetime) {
        if (datetime != null) {
            return dateFormat.format(datetime);
        }
        return null;
    }


    /**
     * 获得20位随机唯一的数字组成的字符串
     */
    public static String getUniqueCode() {
        String number = getUniqueCode(ran);
        ran++;
        if (ran > 999) {
            ran = 100;
        }
        return number;
    }


    /**
     * 返回对象的字符串表示形式 ，如果对象为null则返回空字符串
     */
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 判断是否为 整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {

        if (isNullOrEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("^[-\\+]?[\\d]*$");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }


    /**
     * 判断一个字符串是否含有数字
     *
     * @param content
     * @return
     */
    public static boolean containDigit(String content) {
        if (isNullOrEmpty(content)) {
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
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * 判断字符串是否不为null或空字符串，是则返回true
     */
    public static boolean noNullOrEmpty(String str) {
        return str != null && str.length() > 0;
    }


    /**
     * 判断一个字符串包含多少其他字符串
     */
    public static int strContainStrSum(String str, String key) {
        int count = 0;
        int index = 0;
        int kLen = key.length();
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + kLen;
            count++;
        }
        return count;
    }

    /**
     *文件名在操作系统中不允许出现  / \ " : | * ? < > 故将其以其他字符串替代
     */
    public static String replaceIllegalChar(String str,String rStr){
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(str);
        str= matcher.replaceAll(rStr); // 将匹配到的非法字符替换
        return str;
    }

    /**
     * 对象转化为json字符串（时间属性格式化为yyyy-MM-dd HH:mm:ss）
     */
    public static String object2JsonStr(Object obj){
        return JSONObject.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 比较MB单位的字符串大小
     */
    public static boolean checkMBSize(Long len, String maxSize) {
        maxSize=maxSize.substring(0,maxSize.length()-2);
        double size=Double.parseDouble(maxSize)*1048576;
        if(len>size){
            return true;
        }
        return false;
    }

}
