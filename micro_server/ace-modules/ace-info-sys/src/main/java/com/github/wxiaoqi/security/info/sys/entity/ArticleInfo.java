package com.github.wxiaoqi.security.info.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel(description = "文章实体")
@Table(name = "com_article_info")
@Data
@NoArgsConstructor
public class ArticleInfo {

    @Id
    private Integer id;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

    /**
     * 浏览量
     */
    private String pageviews;

    /**
     * 状态 暂未使用，使用的是父类，的状态id
     */
    @JsonIgnore
    private String status;

    /**
     * 发布时间
     */
    @Column(name = "pub_time")
    private Date pubTime;

    /**
     * 创建时间
     */
    @JsonIgnore
    @Column(name = "crt_time")
    private Date crtTime;

    /**
     * 更新时间
     */
    @JsonIgnore
    @Column(name = "upt_time")
    private Date uptTime;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String simple;

    /**
     * 是否已删除
     */
    @JsonIgnore
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    public ArticleInfo(Integer id, String content) {
        this.id = id;
        this.content = content;
    }
}
