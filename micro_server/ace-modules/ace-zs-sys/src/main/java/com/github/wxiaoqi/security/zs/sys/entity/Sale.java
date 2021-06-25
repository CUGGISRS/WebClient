package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 销售
 */
@Table(name = "aqzs_sale")
@Data
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号(唯一)
     */
    @Column(name = "order_number")
    private String orderNumber;

    /**
     * 产品批次号
     */
    @Column(name = "product_batch_number")
    private String productBatchNumber;

    /**
     * 销售商id
     */
    @Column(name = "seller_id")
    private Integer sellerId;

    /**
     * 销售商名称
     */
    @Column(name = "seller_name")
    private String sellerName;

    /**
     * 发货时间
     */
    @Column(name = "send_date")
    private Date sendDate;

    /**
     * 发货人
     */
    private String sender;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 始发地
     */
    @Column(name = "start_place")
    private String startPlace;

    /**
     * 目的地
     */
    @Column(name = "end_place")
    private String endPlace;

    /**
     * 销售数量
     */
    @Column(name = "sale_amount")
    private BigDecimal saleAmount;

    /**
     * 销售数量单位
     */
    @Column(name = "sale_amount_unit")
    private String saleAmountUnit;

    public Sale(String sellerName) {
        this.sellerName = sellerName;
    }
}
