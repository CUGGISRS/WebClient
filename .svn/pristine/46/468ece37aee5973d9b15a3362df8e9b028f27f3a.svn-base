package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商
 */
@Table(name = "aqzs_supplier")
@Data
@NoArgsConstructor
public class Supplier {

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
     * 联系人
     */
    @Column(name = "contact_person")
    private String contactPerson;

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

    public Supplier(Integer id, Integer isDel) {
        this.id = id;
        this.isDel = isDel;
    }
}
