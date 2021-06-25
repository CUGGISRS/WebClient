package com.github.wxiaoqi.security.dzsw.sys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "dzsw_price_quotation_info")
public class PriceQuotationInfo {
    @Id
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
     * 价格
     */
    private BigDecimal price;

    /**
     * 报价时间
     */
    @Column(name = "offer_time")
    private Date offerTime;

    /**
     * 采集点
     */
    @Column(name = "collection_point")
    private String collectionPoint;

    /**
     * 报价员
     */
    @Column(name = "pricing_offer")
    private String pricingOffer;

    /**
     * 最高价
     */
    @Column(name = "highest_price")
    private BigDecimal highestPrice;

    /**
     * 最低价
     */
    @Column(name = "lowest_price")
    private BigDecimal lowestPrice;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 地区
     */
    private String district;
}
