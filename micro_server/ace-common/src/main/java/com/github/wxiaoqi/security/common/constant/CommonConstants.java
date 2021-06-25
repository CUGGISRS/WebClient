package com.github.wxiaoqi.security.common.constant;

/**
 * Created by ace on 2017/8/29.
 */
public class CommonConstants {
    public final static String RESOURCE_TYPE_MENU = "menu";
    public final static String RESOURCE_TYPE_BTN = "button";
    // 用户token异常
    public static final Integer EX_USER_INVALID_CODE = 40101;
    public static final Integer EX_USER_PASS_INVALID_CODE = 40001;
    // 客户端token异常
    public static final Integer EX_CLIENT_INVALID_CODE = 40301;
    public static final Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    public static final Integer EX_OTHER_CODE = 500;


    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";
    public static final String CONTEXT_KEY_USER_NAME = "currentUser";
    public static final String CONTEXT_KEY_USER_TOKEN = "currentUserToken";

    public static final String CONTEXT_KEY_USER_INFO = "currentUserInfo";
    public static final String JWT_KEY_USER_ID = "userId";
    public static final String JWT_KEY_NAME = "name";


    /**
     * 调用远程服务器前，将文件或文件数组的名称改为远程服务处的参数名
     */
    public final static String FILE_PARAM_NAME="file";
    public final static String FILES_PARAM_NAME="file";

    //请求响应
    public final static String RESP_STATUS ="status";//响应码键值
    public final static String RESP_MESSAGE ="message";//响应信息键值
    public final static Integer RESP_STATUS_SUCCESS =200;//响应码成功值
}
