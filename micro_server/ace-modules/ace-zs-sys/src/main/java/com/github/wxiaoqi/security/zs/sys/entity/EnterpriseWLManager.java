package com.github.wxiaoqi.security.zs.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;

/**
 * 企业物联网管理员
 */
@Table(name = "aqzs_enterprise_wl_manager")
@Data
public class EnterpriseWLManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mailbox;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
