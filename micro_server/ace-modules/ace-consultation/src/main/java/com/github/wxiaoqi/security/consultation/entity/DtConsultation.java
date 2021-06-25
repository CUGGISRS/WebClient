package com.github.wxiaoqi.security.consultation.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dt_consultation")
public class DtConsultation {
    /**
     * 用户token
     */
    @Transient
    private String token;

    /**
     * 获取用户token
     *
     * @return Token - 用户token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置用户token
     *
     * @param token 用户token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 回复编号
     */
    @Id
    @Column(name = "ID")
    private Integer id;

    /**
     * 咨询编号
     */
    @Column(name = "ConsultationID")
    private Integer consultationid;

    /**
     * 发布时间
     */
    @Column(name = "Date")
    private Date date;

    /**
     * 内容
     */
    @Column(name = "Content")
    private String content;

    /**
     * 发布人类型
     */
    @Column(name = "PublishType")
    private String publishtype;

    /**
     * 发布人ID
     */
    @Column(name = "PublishID")
    private Integer publishid;

    /**
     * 发布人名称
     */
    @Column(name = "PublishName")
    private String publishname;

    /**
     * 状态（0-未读 1-已读）
     */
    @Column(name = "State")
    private String state;

    /**
     * 获取回复编号
     *
     * @return ID - 回复编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置回复编号
     *
     * @param id 回复编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取咨询编号
     *
     * @return ConsultationID - 咨询编号
     */
    public Integer getConsultationid() {
        return consultationid;
    }

    /**
     * 设置咨询编号
     *
     * @param consultationid 咨询编号
     */
    public void setConsultationid(Integer consultationid) {
        this.consultationid = consultationid;
    }

    /**
     * 获取发布时间
     *
     * @return Date - 发布时间
     */
    public Date getDate() {
        return date;
    }

    /**
     * 设置发布时间
     *
     * @param date 发布时间
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取内容
     *
     * @return Content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取发布人类型
     *
     * @return PublishType - 发布人类型
     */
    public String getPublishtype() {
        return publishtype;
    }

    /**
     * 设置发布人类型
     *
     * @param publishtype 发布人类型
     */
    public void setPublishtype(String publishtype) {
        this.publishtype = publishtype;
    }

    /**
     * 获取发布人ID
     *
     * @return PublishID - 发布人ID
     */
    public Integer getPublishid() {
        return publishid;
    }

    /**
     * 设置发布人ID
     *
     * @param publishid 发布人ID
     */
    public void setPublishid(Integer publishid) {
        this.publishid = publishid;
    }

    /**
     * 获取发布人名称
     *
     * @return PublishName - 发布人名称
     */
    public String getPublishname() {
        return publishname;
    }

    /**
     * 设置发布人名称
     *
     * @param publishname 发布人名称
     */
    public void setPublishname(String publishname) {
        this.publishname = publishname;
    }

    /**
     * 获取状态（0-未读 1-已读）
     *
     * @return State - 状态（0-未读 1-已读）
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态（0-未读 1-已读）
     *
     * @param state 状态（0-未读 1-已读）
     */
    public void setState(String state) {
        this.state = state;
    }
}
