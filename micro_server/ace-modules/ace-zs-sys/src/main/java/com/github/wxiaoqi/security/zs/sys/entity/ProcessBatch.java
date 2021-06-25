package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加工批次
 */
@Table(name = "aqzs_process_batch")
@Data
@NoArgsConstructor
public class ProcessBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 加工基地id
     */
    @Column(name = "base_id")
    private Integer baseId;

    /**
     * 加工基地名称
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
     * 产品批次号（唯一）
     */
    @Column(name = "product_batch_number")
    private String productBatchNumber;

    /**
     * 加工数量
     */
    @Column(name = "process_amount")
    private BigDecimal processAmount;

    /**
     * 加工数量单位
     */
    @Column(name = "process_amount_unit")
    private String processAmountUnit;

    /**
     * 收获表id
     */
    @Column(name = "harvest_id")
    private Integer harvestId;
    /**
     * 收购表id
     */
    @Column(name = "buy_id")
    private Integer buyId;

    /**
     * 加工方式
     */
    @Column(name = "process_way")
    private String processWay;

    /**
     * 产品详情表id
     */
    @Column(name = "product_detail_id")
    private Integer productDetailId;

    /**
     * 产品批次号图片
     */
    @Transient
    private List<FileInfo> productBatchNumberPictureList;

    public ProcessBatch(String baseName) {
        this.baseName = baseName;
    }
}
