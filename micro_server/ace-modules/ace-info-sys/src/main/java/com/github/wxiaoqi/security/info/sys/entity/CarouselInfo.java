package com.github.wxiaoqi.security.info.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@ApiModel
@Table(name = "com_carousel_info")
public class CarouselInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 图片
     */
    private Integer img;

    /**
     * 目标系统
     */
    @ApiModelProperty(value = "目标系统")
    @Column(name = "target_system")
    private String targetSystem;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    @Column(name = "article_id")
    private Integer articleId;

    /**
     * 文章类型
     */
    @ApiModelProperty(value = "文章类型")
    @Column(name = "article_typeid")
    private String articleTypeid;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String publisher;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String url;

    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号")
    private Integer orderId;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取图片
     *
     * @return img - 图片
     */
    public Integer getImg() {
        return img;
    }

    /**
     * 设置图片
     *
     * @param img 图片
     */
    public void setImg(Integer img) {
        this.img = img;
    }

    /**
     * 获取目标系统
     *
     * @return target_system - 目标系统
     */
    public String getTargetSystem() {
        return targetSystem;
    }

    /**
     * 设置目标系统
     *
     * @param targetSystem 目标系统
     */
    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    /**
     * 获取文章ID
     *
     * @return article_id - 文章ID
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * 设置文章ID
     *
     * @param articleId 文章ID
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取文章类型
     *
     * @return article_typeid - 文章类型
     */
    public String getArticleTypeid() {
        return articleTypeid;
    }

    /**
     * 设置文章类型
     *
     * @param articleTypeid 文章类型
     */
    public void setArticleTypeid(String articleTypeid) {
        this.articleTypeid = articleTypeid;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取发布人
     *
     * @return publisher - 发布人
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 设置发布人
     *
     * @param publisher 发布人
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * 获取跳转链接
     *
     * @return url - 跳转链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置跳转链接
     *
     * @param url 跳转链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取排序序号
     *
     * @return order - 排序序号
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置排序序号
     *
     * @param order 排序序号
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}