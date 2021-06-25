package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 产品
 */
@Table(name = "aqzs_product")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 产品详情表id
     */
    @Column(name = "product_detail_id")
    private Integer productDetailId;


    /**
     * 基地id
     */
    @Column(name = "base_id")
    private Integer baseId;

    /**
     * 基地名称
     */
    @Column(name = "base_name")
    private String baseName;

    /**
     * 开始时间
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 图片
     */
    @Transient
    private List<FileInfo> pictureList;

    public Product(String baseName) {
        this.baseName = baseName;
    }
}
