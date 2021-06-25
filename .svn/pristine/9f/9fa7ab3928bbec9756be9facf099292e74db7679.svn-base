package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检验_项目
 */
@Table(name = "aqzs_between_test_item")
@Data
@NoArgsConstructor
public class BetweenTestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 检验表id
     */
    @Column(name = "test_id")
    private Integer testId;

    /**
     * 检验类型
     */
    @Column(name = "test_type")
    private String testType;

    /**
     * 项目id
     */
    @Column(name = "item_id")
    private Integer itemId;

    /**
     * 项目名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 检验结果
     */
    @Column(name = "test_result")
    private String testResult;

    public BetweenTestItem(String itemName) {
        this.itemName = itemName;
    }
}
