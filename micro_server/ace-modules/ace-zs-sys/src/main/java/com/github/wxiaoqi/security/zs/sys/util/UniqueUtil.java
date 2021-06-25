package com.github.wxiaoqi.security.zs.sys.util;

import com.github.wxiaoqi.security.common.util.StringHelper;

import java.text.SimpleDateFormat;

/**
 * 唯一码工具类
 */
public class UniqueUtil {

    private static int ran = 100;

    public static String getUniqueCode(int ran) {
        long now = System.currentTimeMillis();
        //获取4位年份数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("SSSMMmmHHyyyy" + ran + "ddss");
        //获取时间戳
        String time = dateFormat.format(now);
        //获取三位随机数    要是一段时间内的数据连过大会有重复的情况，所以再套一层方法
        //    int ran=(int) ((Math.random()*9+1)*100);
        return time;
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


}
