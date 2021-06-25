package com.github.wxiaoqi.security.com.sys.constatns;

/**
 * 通用常量
 */
public class CommonConstants {

    public final static int ROOT = -1;

    //表文件类型  区分一个表中多个多文件字段
    public final static Integer FILE_TYPE_1 = 1;
    public final static Integer FILE_TYPE_2 = 2;
    public final static Integer FILE_TYPE_3 = 3;

    //表名
    public final static String FORM_NAME_1 = "aqjd_between_dept_user";//部门-用户
    public final static String FORM_NAME_2 = "aqjd_dept";//部门
    public final static String FORM_NAME_3 = "aqjd_dept_test";//部门抽检
    public final static String FORM_NAME_4 = "aqzs_base";//基地
    public final static String FORM_NAME_5 = "aqzs_base_work";//基本作业
    public final static String FORM_NAME_6 = "aqzs_between_enterprise_user";//企业-用户
    public final static String FORM_NAME_7 = "aqzs_between_test_item";//检验-项目
    public final static String FORM_NAME_8 = "aqzs_enterprise";//企业
    public final static String FORM_NAME_9 = "aqzs_enterprise_qualification";//企业资质
    public final static String FORM_NAME_10 = "aqzs_finish_product_test";//成品检验
    public final static String FORM_NAME_11 = "aqzs_grow_environment";//生长环境
    public final static String FORM_NAME_12 = "aqzs_harvest";//收获
    public final static String FORM_NAME_13 = "aqzs_item";//项目
    public final static String FORM_NAME_14 = "aqzs_process_batch";//加工批次
    public final static String FORM_NAME_15 = "aqzs_product";//产品
    public final static String FORM_NAME_16 = "aqzs_regular_test";//定期检验
    public final static String FORM_NAME_17 = "aqzs_sale";//销售
    public final static String FORM_NAME_18 = "aqzs_seller";//销售商
    public final static String FORM_NAME_19 = "com_data_dictionary";//数据字典
    public final static String FORM_NAME_20 = "com_file";//文件
    public final static String FORM_NAME_21 = "base_user";//用户
    public final static String FORM_NAME_22 = "aqzs_product_detail";//产品详情
    public final static String FORM_NAME_23 = "aqzs_buy";//收购
    public final static String FORM_NAME_24 = "xph_device_sensor";//物联网设备传感器
    public final static String FORM_NAME_25 = "xph_device_group";//物联网设备分组

    /**
     * 调用远程服务器前，将文件或文件数组的名称改为远程服务处的参数名
     */
    public final static String FILE_PARAM_NAME = "file";
    public final static String FILES_PARAM_NAME = "file";

    //检验类型
    public final static String TEST_TYPE_1 = "JL1";//定期检验
    public final static String TEST_TYPE_2 = "JL2";//成品检验
    public final static String TEST_TYPE_3 = "JL3";//部门抽检

    public static final String JWT_KEY_USER = "user";

    public static final String[] MONTH_NAME = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    //基地类型
    public final static String BASE_TYPE_1 = "养殖基地";
    public final static String BASE_TYPE_2 = "种植基地";
    public final static String BASE_TYPE_3 = "加工基地";

    //字典数据类型
    public final static String DATA_TYPE_1 = "镇乡";

    //系统模块
    public final static String SYS_MODULE_SC_KEY = "sc";
    public final static String SYS_MODULE_ZZ_KEY = "zz";
    public final static Integer SYS_MODULE_SC = 1;//水产
    public final static Integer SYS_MODULE_ZZ = 2;//种植

    //检验结果
    public final static String TEST_RESULT_1 = "合格";
    public final static String TEST_RESULT_2 = "未合格";


    //物联网供应类型
    public final static String WL_XPH = "WG1"; //新普惠
    public final static String WL_TP = "WG2";//托普

    //物联网token键值
    public final static String WL_TOKEN_KEY = "token"; //新普惠

    //用户类型
    public final static Integer USER_TYPE_1 = 1;//个人用户
    public final static Integer USER_TYPE_2 = 2;//企业用户
    public final static Integer USER_TYPE_3 = 3;//部门用户
}
