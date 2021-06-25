package com.github.wxiaoqi.security.jd.sys.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * 部门-用户
 */
@Table(name = "aqjd_between_dept_user")
@Data
public class BetweenDeptUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属部门id
     */
    @Column(name = "dept_id")
    private Integer deptId;


    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;


}
