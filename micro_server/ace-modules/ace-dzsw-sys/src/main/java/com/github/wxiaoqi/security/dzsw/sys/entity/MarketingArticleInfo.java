package com.github.wxiaoqi.security.dzsw.sys.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@NoArgsConstructor
@Table(name = "dzsw_marketing_article_info")
public class MarketingArticleInfo {
    @Id
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 产品分类
     */
    @Column(name = "product_category")
    private String productCategory;

    /**
     * 阅读量
     */
    private Integer reading;

    /**
     * 来源
     */
    private String source;

    /**
     * 发布时间
     */
    @Column(name = "pub_time")
    private Date pubTime;

    /**
     * 作者
     */
    private String author;

    /**
     * 状态（待发布，已发布）
     */
    private String status;

    /**
     * 市场行情/产销信息
     */
    private String type;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 内容
     */
    private String content;

    public MarketingArticleInfo(Integer id, String content) {
        this.id = id;
        this.content = content;
    }
}
