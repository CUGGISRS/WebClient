package com.github.wxiaoqi.security.dzsw.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "dzsw_quality_products_info")
public class QualityProductsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 产品分类
     */
    @Column(name = "product_category")
    private String productCategory;

    /**
     * 产品图片
     */
    @Column(name = "product_picture")
    private Integer productPicture;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 描述
     */
    private String description;

    /**
     * 产品产地
     */
    @Column(name = "product_origin")
    private String productOrigin;

    /**
     * 购买方式
     */
    @Column(name = "purchase_way")
    private String purchaseWay;

    /**
     * 更新时间
     */
    @Column(name = "upt_time")
    private Date uptTime;

    /**
     * 发布时间
     */
    @Column(name = "pub_time")
    private Date pubTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 热度
     */
    private Integer popularity;

    /**
     * 推荐人ID，也就是用户ID
     */
    @Column(name = "referrerID")
    private Integer referrerid;

    /**
     * 推荐人名称
     */
    private String referrer;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    /**
     * 图片url列表
     */
    @Transient
    private List<String> urlList;

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
     * 获取产品名称
     *
     * @return product_name - 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置产品名称
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取产品分类
     *
     * @return product_category - 产品分类
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * 设置产品分类
     *
     * @param productCategory 产品分类
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    /**
     * 获取产品图片
     *
     * @return product_picture - 产品图片
     */
    public Integer getProductPicture() {
        return productPicture;
    }

    /**
     * 设置产品图片
     *
     * @param productPicture 产品图片
     */
    public void setProductPicture(Integer productPicture) {
        this.productPicture = productPicture;
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取产品产地
     *
     * @return product_origin - 产品产地
     */
    public String getProductOrigin() {
        return productOrigin;
    }

    /**
     * 设置产品产地
     *
     * @param productOrigin 产品产地
     */
    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    /**
     * 获取购买方式
     *
     * @return purchase_way - 购买方式
     */
    public String getPurchaseWay() {
        return purchaseWay;
    }

    /**
     * 设置购买方式
     *
     * @param purchaseWay 购买方式
     */
    public void setPurchaseWay(String purchaseWay) {
        this.purchaseWay = purchaseWay;
    }

    /**
     * 获取更新时间
     *
     * @return upt_time - 更新时间
     */
    public Date getUptTime() {
        return uptTime;
    }

    /**
     * 设置更新时间
     *
     * @param uptTime 更新时间
     */
    public void setUptTime(Date uptTime) {
        this.uptTime = uptTime;
    }

    /**
     * 获取发布时间
     *
     * @return pub_time - 发布时间
     */
    public Date getPubTime() {
        return pubTime;
    }

    /**
     * 设置发布时间
     *
     * @param pubTime 发布时间
     */
    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
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
     * 获取热度
     *
     * @return popularity - 热度
     */
    public Integer getPopularity() {
        return popularity;
    }

    /**
     * 设置热度
     *
     * @param popularity 热度
     */
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    /**
     * 获取推荐人ID，也就是用户ID
     *
     * @return referrerID - 推荐人ID，也就是用户ID
     */
    public Integer getReferrerid() {
        return referrerid;
    }

    /**
     * 设置推荐人ID，也就是用户ID
     *
     * @param referrerid 推荐人ID，也就是用户ID
     */
    public void setReferrerid(Integer referrerid) {
        this.referrerid = referrerid;
    }

    /**
     * 获取推荐人名称
     *
     * @return referrer - 推荐人名称
     */
    public String getReferrer() {
        return referrer;
    }

    /**
     * 设置推荐人名称
     *
     * @param referrer 推荐人名称
     */
    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    /**
     * @return is_deleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
