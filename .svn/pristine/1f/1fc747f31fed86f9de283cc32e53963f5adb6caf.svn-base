package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 销售商
 */
@Table(name = "aqzs_seller")
@Data
@NoArgsConstructor
public class Seller {
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
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mailbox;

    /**
     * 联系人
     */
    @Column(name = "contact_person")
    private String contactPerson;

    /**
     * 销售许可证号
     */
    @Column(name = "sale_license_number")
    private String saleLicenseNumber;

    /**
     * 组织机构代码
     */
    @Column(name = "organization_code")
    private String organizationCode;

    /**
     * 是否向公众公开（1是，0否）
     */
    @Column(name = "is_open")
    private Integer isOpen;

    /**
     * 性质
     */
    private String nature;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 是否删除（0否，1是）
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 企业名称
     */
    @Column(name = "enterprise_name")
    private String enterpriseName;

    public Seller(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Seller(Integer id, Integer isDel) {
        this.id = id;
        this.isDel = isDel;
    }
}
