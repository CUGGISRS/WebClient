package com.github.wxiaoqi.security.com.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 */
@Table(name = "base_user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 类型（1个人用户、2企业用户、3部门用户）
     */
    private Integer type;

    /**
     * 生日
     */
    private Date birthday;

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
     * 性别
     */
    private String sex;

    /**
     * 状态(0待审核，1正常，2禁用)
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否认证(0未1已)
     */
    @Column(name = "is_verify")
    private Integer isVerify;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 系统名称
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 目标系统
     */
    @Column(name = "target_system")
    private String targetSystem;

    /**
     * 注册时间
     */
    @Column(name = "reg_time")
    private Date regTime;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    public User(Integer id, String photo) {
        this.id = id;
        this.photo = photo;
    }

    public User(String name, String username, String password, Integer type, String sysName) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.type = type;
        this.sysName = sysName;
    }

    public User(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }
}
