package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基本作业
 */
@Table(name = "aqzs_base_work")
@Data
@NoArgsConstructor
public class BaseWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 作业类型
     */
    @Column(name = "work_type")
    private String workType;

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
     * 作业物名称
     */
    @Column(name = "work_object_name")
    private String workObjectName;

    /**
     * 作业物类型
     */
    @Column(name = "work_object_type")
    private String workObjectType;

    /**
     * 作业物来源
     */
    @Column(name = "work_object_source")
    private String workObjectSource;

    /**
     * 作业物数量
     */
    @Column(name = "work_object_amount")
    private BigDecimal workObjectAmount;

    /**
     * 作业物数量单位
     */
    @Column(name = "work_object_amount_unit")
    private String workObjectAmountUnit;

    /**
     * 操作人id（关联用户表id）
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 天气状况
     */
    private String weather;

    /**
     * 监督人id（关联用户表id）
     */
    @Column(name = "supervisor_id")
    private Integer supervisorId;

    /**
     * 监督人
     */
    private String supervisor;

    /**
     * 监督结果
     */
    @Column(name = "supervisor_result")
    private String supervisorResult;

    /**
     * 作业图片
     */
    @Transient
    private List<FileInfo> workPictureList;

    public BaseWork(String productName) {
        this.productName = productName;
    }
}
