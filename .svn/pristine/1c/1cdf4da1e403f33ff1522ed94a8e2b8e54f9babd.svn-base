package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 产品详情
 */
@Table(name = "aqzs_product_detail")
@Data
@NoArgsConstructor
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品条码（唯一）
     */
    private String code;

    /**
     * 类型
     */
    private String type;

    /**
     * 规格
     */
    private String specs;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 状态（1在产，0停产）
     */
    private Integer status;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 企业名称
     */
    @Column(name = "enterprise_name")
    private String enterpriseName;

    /**
     * 保质期
     */
    @Column(name = "shelf_life")
    private String shelfLife;

    /**
     * 系统模块(1水产，2种植)
     */
    @Column(name = "sys_module")
    private Integer sysModule;

    /**
     * 是否删除（0否，1是）
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 图片
     */
    @Transient
    private List<FileInfo> pictureList;
    /**
     * 认证证书
     */
    @Transient
    private List<FileInfo> certificateList;

    public ProductDetail(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public ProductDetail(Integer id, Integer isDel) {
        this.id = id;
        this.isDel = isDel;
    }
}
