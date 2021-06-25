package com.github.wxiaoqi.security.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "agriculture.gate_log")
public class GateLog {
    @Id
    private Integer id;

    /**
     * 菜单
     */
    private String menu;

    /**
     * 操作
     */
    private String opt;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 操作时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "crt_time")
    private Date crtTime;

    /**
     * 操作人ID
     */
    @Column(name = "crt_user")
    private String crtUser;

    /**
     * 操作人名称
     */
    @Column(name = "crt_name")
    private String crtName;

    /**
     * 操作主机IP
     */
    @Column(name = "crt_host")
    private String crtHost;

    /**
     * 请求类型
     */
    private String type;

    /**
     * url后拼接参数
     */
    @Column(name = "url_param")
    private String urlParam;

    /**
     * 请求头
     */
    private String header;

    /**
     * 请求体
     */
    private String body;

    /**
     * 响应信息
     */
    @Column(name = "response_info")
    private String responseInfo;

    /**
     * 获取序号
     *
     * @return id - 序号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置序号
     *
     * @param id 序号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取菜单
     *
     * @return menu - 菜单
     */
    public String getMenu() {
        return menu;
    }

    /**
     * 设置菜单
     *
     * @param menu 菜单
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * 获取操作
     *
     * @return opt - 操作
     */
    public String getOpt() {
        return opt;
    }

    /**
     * 设置操作
     *
     * @param opt 操作
     */
    public void setOpt(String opt) {
        this.opt = opt;
    }

    /**
     * 获取资源路径
     *
     * @return uri - 资源路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置资源路径
     *
     * @param uri 资源路径
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 获取操作时间
     *
     * @return crt_time - 操作时间
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置操作时间
     *
     * @param crtTime 操作时间
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取操作人ID
     *
     * @return crt_user - 操作人ID
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * 设置操作人ID
     *
     * @param crtUser 操作人ID
     */
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * 获取操作人名称
     *
     * @return crt_name - 操作人名称
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * 设置操作人名称
     *
     * @param crtName 操作人名称
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * 获取操作主机IP
     *
     * @return crt_host - 操作主机IP
     */
    public String getCrtHost() {
        return crtHost;
    }

    /**
     * 设置操作主机IP
     *
     * @param crtHost 操作主机IP
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    /**
     * 获取请求类型
     *
     * @return type - 请求类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置请求类型
     *
     * @param type 请求类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取url后拼接参数
     *
     * @return url_param - url后拼接参数
     */
    public String getUrlParam() {
        return urlParam;
    }

    /**
     * 设置url后拼接参数
     *
     * @param urlParam url后拼接参数
     */
    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    /**
     * 获取请求头
     *
     * @return header - 请求头
     */
    public String getHeader() {
        return header;
    }

    /**
     * 设置请求头
     *
     * @param header 请求头
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 获取请求体
     *
     * @return body - 请求体
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置请求体
     *
     * @param body 请求体
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 获取响应信息
     *
     * @return response_info - 响应信息
     */
    public String getResponseInfo() {
        return responseInfo;
    }

    /**
     * 设置响应信息
     *
     * @param responseInfo 响应信息
     */
    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }
}
