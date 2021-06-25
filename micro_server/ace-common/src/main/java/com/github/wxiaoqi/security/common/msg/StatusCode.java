package com.github.wxiaoqi.security.common.msg;

/**
 * @author TsaiJun
 */

public enum StatusCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功! "),
    /**
     * 失败
     */
    FAIL(500, "失败! "),
    /**
     * 请求参数错误
     */
    REQUEST_PARAM_ERROR(501, "请求参数错误! "),
    /**
     * 文件删除成功
     */
    FILEDELETESUCCESS(200, "文件删除成功! "),
    /**
     * 文件删除失败
     */
    FILEDELETEFAIL(500, "文件删除失败! "),

    FILENULL(501, "文件不存在! "),

    FILEUPLOADFAIL(502, "文件上传失败! "),

    E_500(500, "请求方式有误,请检查 GET/POST"),

    E_10009(10009, "账户已存在"),

    USER_LOGIN_ERROR3(1106, "验证码获取过于频繁，请 & 秒后点击【重新获取】尝试"),
    USER_CODE_TOOMANY(1109, "今日获取验证码次数已达上限"),
    SMS_SEND_ERROR(1110,"验证码发送失败"),
    SMS_ERROR(1111,"验证码错误"),
    REGISTER_ENABLE(200,"用户名可用"),
    REGISTER_UNABLE(500,"用户名重复，不可用")
    ;



    /**
     * 状态码
     */
    private int status;

    /**
     * 返回信息
     */
    private String message;

    StatusCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}