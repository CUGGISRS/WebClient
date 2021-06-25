package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定期检验
 */
@Table(name = "aqzs_regular_test")
@Data
@NoArgsConstructor
public class RegularTest {
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
     * 检验数量
     */
    @Column(name = "test_amount")
    private BigDecimal testAmount;

    /**
     * 检验数量单位
     */
    @Column(name = "test_amount_unit")
    private String testAmountUnit;

    /**
     * 检验时间
     */
    @Column(name = "test_date")
    private Date testDate;

    /**
     * 检验结果
     */
    @Column(name = "test_result")
    private String testResult;

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
     * 检验结果图片
     */
    @Transient
    private List<FileInfo> testResultPictureList;

    /**
     * 检验_项目
     */
    @Transient
    private List<BetweenTestItem> betweenTestItems;
    /**
     * 项目
     */
    @Transient
    private List<ItemInfo> itemInfos;

    public RegularTest(String productName) {
        this.productName = productName;
    }
}
