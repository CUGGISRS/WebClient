package com.github.wxiaoqi.security.news.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

public class News {
    @Id
    private Integer newsid;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类
     */
    private String type;

    private String author;

    //@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date updatetime;

    private String status;

    /**
     * 内容
     */
    private String content;

    private byte[] image;

    /**
     * @return newsid
     */
    public Integer getNewsid() {
        return newsid;
    }

    /**
     * @param newsid
     */
    public void setNewsid(Integer newsid) {
        this.newsid = newsid;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
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
     * 获取分类
     *
     * @return type - 分类
     */
    public String getType() {
        return type;
    }

    /**
     * 设置分类
     *
     * @param type 分类
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return updatetime
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
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
     * @return image
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * @param image
     */
    public void setImage(byte[] image) {
        this.image = image;
    }
}
