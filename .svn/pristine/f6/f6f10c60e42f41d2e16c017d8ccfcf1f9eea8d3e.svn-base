package com.github.wxiaoqi.security.jd.sys.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * 监督部门
 */
@Table(name = "aqjd_dept")
@Data
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 类型
     */
    private String type;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮编
     */
    private String postcode;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private Integer parentId;
}
