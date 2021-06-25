package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基地
 */
@Table(name = "aqzs_base")
@Data
@NoArgsConstructor
public class BaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 面积（亩）
     */
    private BigDecimal area;

    /**
     * 基地类型
     */
    @Column(name = "base_type")
    private String baseType;

    /**
     * 地址
     */
    private String address;

    /**
     * 负责人
     */
    private String lender;

    /**
     * 负责人电话
     */
    @Column(name = "lender_phone")
    private String lenderPhone;

    /**
     * 环境检验报告是否合格（1是，0否）
     */
    @Column(name = "is_report_qualified")
    private Integer isReportQualified;

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
     * 是否删除（0否，1是）
     */
    @Column(name = "is_del")
    private Integer isDel;


    /**
     * 环境检验报告图片
     */
    @Transient
    private List<FileInfo> reportPictureList;


    /**
     * 基地图片
     */
    @Transient
    private List<FileInfo> basePictureList;

    public BaseInfo(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public BaseInfo(Integer id, Integer isDel) {
        this.id = id;
        this.isDel = isDel;
    }
}
