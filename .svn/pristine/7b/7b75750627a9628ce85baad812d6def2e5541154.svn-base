package com.github.wxiaoqi.security.dzsw.sys.constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 通用常量
 */
public class CommonConstants {

    public static final String JWT_KEY_USER = "user";

    public static final String SYS_DZSW = "电子商务系统";
    public static final String SYS_GYX = "公益性系统";
    public static final String SYS_ZSXX = "追溯信息系统";

    // 审核通过、待审核
    public static final String EX_PASSED = "审核通过";
    public static final String EX_PENDING = "待审核";

    // 待发布、已发布
    public static final String TO_BE_RELEASED = "待发布";
    public static final String PUBLISHED = "已发布";

    public static final String ULTABLE_DQPI = "dzsw_quality_products_info"; // 优质农产品表
    public static final String ULTABLE_CCL = "com_carousel_info";  // 轮播图表
    public static final String ULTABLE_CVI = "com_video_info";  // 视频信息表
    public static final String ULTABLE_GSDI = "gyx_supply_demand_info"; // 供求信息表
    public static final String ULTABLE_ZCI = "zsxx_company_info"; //企业信息表
    public static final String ULTABLE_ZPAI = "zsxx_products_agricultural_info"; //特色农产品信息表

    public static String serverIp; //本机ip
    public static String serverIpAndPort; //本机ip + 端口

    static {
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


//    public enum targetSystem {
//        DZSW,
//        ZSXX,
//        GYX;
//    }

}
