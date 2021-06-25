package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收购
 */
@Table(name = "aqzs_buy")
@Data
@NoArgsConstructor
public class BuyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 追溯码
     */
    @Column(name = "trace_code")
    private String traceCode;

    /**
     * 收购产品名称
     */
    private String name;

    /**
     * 收购总价(元)
     */
    @Column(name = "buy_total")
    private BigDecimal buyTotal;

    /**
     * 收购数量
     */
    @Column(name = "buy_amount")
    private BigDecimal buyAmount;

    /**
     * 收购数量单位
     */
    @Column(name = "buy_amount_unit")
    private String buyAmountUnit;

    /**
     * 收购时间
     */
    @Column(name = "buy_date")
    private Date buyDate;

    /**
     * 描述
     */
    private String description;

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
     * 系统模块(1水产，2种植)
     */
    @Column(name = "sys_module")
    private Integer sysModule;

    /**
     * 收购图片
     */
    @Transient
    private List<FileInfo> buyPictureList;

    public BuyInfo(String name, String enterpriseName) {
        this.name = name;
        this.enterpriseName = enterpriseName;
    }
}
