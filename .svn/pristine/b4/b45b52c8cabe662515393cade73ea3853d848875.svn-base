package com.github.wxiaoqi.security.zs.sys.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.common.util.URLUtil;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用物联网相关接口的工具类
 */
public class WLUtil {

    /**
     * 获取物联网供应的token
     */
    public static String getWLToken(String username, String password, String type) {
        //新普惠接口
        if (CommonConstants.WL_XPH.equals(type)) {
            JSONObject params = new JSONObject();
            params.put("username", username);
            params.put("password", password);

            String resultStr = URLUtil.postOrPutJson(
                    "http://47.105.215.208:8005/login",
                    "POST", params, null, null);
            JSONObject result = URLUtil.string2JSONObject(resultStr, false);
            if (result != null) {
                String wlToken = result.getString(CommonConstants.WL_TOKEN_KEY);
                return wlToken;
            }
        }
        return null;
    }

    /**
     * 获取物联网供应的某一设备（记录时间大于等于recordStart,小于等于recordEnd)的总数
     */
    public static long getSupplyDeviceDataTotal(String token, Integer deviceId,
                                                Date recordStart, Date recordEnd, String type) {
        long total = 0;
        if (deviceId != null) {
            //新普惠接口
            if (CommonConstants.WL_XPH.equals(type)) {
                Map<String, String> params = new HashMap<>();
                String start = StringHelper.dateYMdHMS2String(recordStart);
                String end = StringHelper.dateYMdHMS2String(recordEnd);
                //url上特殊字符问题
                start = StringHelper.string2URLEncoder(start);
                end = StringHelper.string2URLEncoder(end);
                params.put("startTime", start);
                params.put("endTime", end);

                String resultStr = URLUtil.getOrDelete(
                        "http://47.105.215.208:8005/datas/" + deviceId + "/count",
                        "GET", params, token, CommonConstants.WL_TOKEN_KEY);
                JSONObject result = URLUtil.string2JSONObject(resultStr, true);
                if (result != null) {
                    total = Integer.parseInt(result.getString("Long"));
                }
            }

        }
        return total;
    }

    /**
     * 获取物联网供应的某一设备（记录时间大于等于recordStart,小于等于recordEnd)数据
     */
    public static JSONArray getSupplyDeviceData(String token, Integer deviceId,
                                                Date recordStart, Date recordEnd,
                                                String page, String limit,
                                                String type) {
        if (deviceId != null) {
            //新普惠接口
            if (CommonConstants.WL_XPH.equals(type)) {
                Map<String, String> params = new HashMap<>();
                params.put("pageNum", page);
                params.put("pageSize", limit);
                //url上特殊字符问题
                String start = StringHelper.dateYMdHMS2String(recordStart);
                String end = StringHelper.dateYMdHMS2String(recordEnd);
                start = StringHelper.string2URLEncoder(start);
                end = StringHelper.string2URLEncoder(end);
                params.put("startTime", start);
                params.put("endTime", end);

                String resultStr = URLUtil.getOrDelete(
                        "http://47.105.215.208:8005/datas/" + deviceId,
                        "GET", params, token, CommonConstants.WL_TOKEN_KEY);
                JSONArray result = URLUtil.string2JSONArray(resultStr, "item");
                if (result != null) {
                    return result;
                }
            }

        }
        return null;
    }

    public static void main(String[] args) {
 /*       String a="0.56454353645";
        int c=0;
        double b=Double.parseDouble(a);
        double d=c*b;
        System.out.println(MyObjectUtil.getSixDecimal(d));
        c=10;
        d=c*b;
        System.out.println(MyObjectUtil.getSixDecimal(d));
*/

        String token = getWLToken("test", "123456", "WG1");
        long t = getSupplyDeviceDataTotal(token, 15112501, new Date(0), new Date(), "WG1");
        System.out.println(t);
        JSONArray a = getSupplyDeviceData(token, 15112501, new Date(0), new Date(), "1", "10", "WG1");
        Date x = StringHelper.stringYMdHMS2Date("2020-12-18 11:52:07");
        long xl = x.getTime();
        for (int i = 0; i < a.size(); i++) {
            JSONObject o = a.getJSONObject(i);
            String dataTime = o.getString("dataTime");
            Date d = StringHelper.stringYMdHMS2Date(dataTime);

            if (d.getTime() > xl) {
                System.out.println(i + "--" + a.get(i).toString());
            }
        }

        a = MyObjectUtil.sortByDateStr(a, "dataTime");
        for (int i = 0; i < a.size(); i++) {
            JSONObject o = a.getJSONObject(i);
            String dataTime = o.getString("dataTime");
            Date d = StringHelper.stringYMdHMS2Date(dataTime);

            if (d.getTime() > xl) {
                System.out.println("排序：" + i + "--" + a.get(i).toString());
            }
        }
    }


}
