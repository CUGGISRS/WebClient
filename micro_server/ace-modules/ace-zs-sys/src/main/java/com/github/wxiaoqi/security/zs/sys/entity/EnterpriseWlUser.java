package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * 企业物联网用户
 */
@Table(name = "aqzs_enterprise_wl_user")
@Data
public class EnterpriseWlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 名称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 供应类型
     */
    private String type;
}
