package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业_用户
 */
@Table(name = "aqzs_between_enterprise_user")
@Data
@NoArgsConstructor
public class BetweenEnterpriseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;


    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;


    public BetweenEnterpriseUser(Integer enterpriseId, Integer userId) {
        this.enterpriseId = enterpriseId;
        this.userId = userId;
    }

}
