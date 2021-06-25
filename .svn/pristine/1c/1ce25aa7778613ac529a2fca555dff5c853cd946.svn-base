package com.zd.earth.manage.msg;

/**
 * @author TsaiJun
 */

public enum StatusCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功! "),

    /**
     * 成功
     */
    ADD_SUCCESS(200, "添加"),
    /**
     * 成功
     */
    EDIT_SUCCESS(200, "修改"),


    /**
     * 失败
     */
    FAIL(500, "失败! "),
    /**
     * 请求参数错误
     */
    REQUEST_PARAM_ERROR(501, "请求参数错误! ")
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
