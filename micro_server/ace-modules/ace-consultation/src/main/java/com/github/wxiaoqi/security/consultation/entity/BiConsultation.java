package com.github.wxiaoqi.security.consultation.entity;

import lombok.Data;
import org.jboss.logging.Field;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "bi_consultation")
public class BiConsultation {
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
     * 资讯编号
     */
    @Id
    @Column(name = "ID")
    private Integer id;

    /**
     * 标题
     */
    @Column(name = "Title")
    private String title;

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
     * 评分（1-非常不满意 2-不满意 3-满意 4-很满意 5-非常满意）
     */
    @Column(name = "Score")
    private Double score;

    /**
     * 专家ID
     */
    @Column(name = "ExpertID")
    private Integer expertid;

    /**
     * 专家姓名
     */
    @Column(name = "ExpertName")
    private String expertname;

    /**
     * 用户ID
     */
    @Column(name = "UserID")
    private Integer userid;

    /**
     * 用户名称
     */
    @Column(name = "UserName")
    private String username;

    /**
     * 状态（0-未读 1-已读 2-已解决 3-已关闭）
     */
    @Column(name = "State")
    private String state;

    /**
     * 获取资讯编号
     *
     * @return ID - 资讯编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置资讯编号
     *
     * @param id 资讯编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取标题
     *
     * @return Title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
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
     * 获取评分（1-非常不满意 2-不满意 3-满意 4-很满意 5-非常满意）
     *
     * @return Score - 评分（1-非常不满意 2-不满意 3-满意 4-很满意 5-非常满意）
     */
    public Double getScore() {
        return score;
    }

    /**
     * 设置评分（1-非常不满意 2-不满意 3-满意 4-很满意 5-非常满意）
     *
     * @param score 评分（1-非常不满意 2-不满意 3-满意 4-很满意 5-非常满意）
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * 获取专家ID
     *
     * @return ExpertID - 专家ID
     */
    public Integer getExpertid() {
        return expertid;
    }

    /**
     * 设置专家ID
     *
     * @param expertid 专家ID
     */
    public void setExpertid(Integer expertid) {
        this.expertid = expertid;
    }

    /**
     * 获取专家姓名
     *
     * @return ExpertName - 专家姓名
     */
    public String getExpertname() {
        return expertname;
    }

    /**
     * 设置专家姓名
     *
     * @param expertname 专家姓名
     */
    public void setExpertname(String expertname) {
        this.expertname = expertname;
    }

    /**
     * 获取用户ID
     *
     * @return UserID - 用户ID
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户ID
     *
     * @param userid 用户ID
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取用户名称
     *
     * @return UserName - 用户名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名称
     *
     * @param username 用户名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取状态（0-未读 1-已读 2-已解决 3-已关闭）
     *
     * @return State - 状态（0-未读 1-已读 2-已解决 3-已关闭）
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态（0-未读 1-已读 2-已解决 3-已关闭）
     *
     * @param state 状态（0-未读 1-已读 2-已解决 3-已关闭）
     */
    public void setState(String state) {
        this.state = state;
    }
}
