package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收获
 */
@Table(name = "aqzs_harvest")
@Data
@NoArgsConstructor
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 追溯码（唯一）
     */
    @Column(name = "trace_code")
    private String traceCode;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 收获数量
     */
    @Column(name = "harvest_amount")
    private BigDecimal harvestAmount;

    /**
     * 收获数量单位
     */
    @Column(name = "harvest_amount_unit")
    private String harvestAmountUnit;

    /**
     * 收获方式
     */
    @Column(name = "harvest_way")
    private String harvestWay;

    /**
     * 收获时间
     */
    @Column(name = "harvest_date")
    private Date harvestDate;

    /**
     * 天气状况
     */
    private String weather;

    /**
     * 收获图片
     */
    @Transient
    private List<FileInfo> harvestPictureList;

    public Harvest(String productName) {
        this.productName = productName;
    }
}
