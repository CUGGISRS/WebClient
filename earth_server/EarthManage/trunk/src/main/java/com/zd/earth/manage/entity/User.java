package com.zd.earth.manage.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 */
@Table(name = "user")
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
     * 类型（1管理员、2普通用户）
     */
    private Integer type;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 单位名称
     */
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 单位地址
     */
    @Column(name = "unit_address")
    private String unitAddress;

    /**
     * 状态(0禁用，1启用)
     */
    private Integer status;

    /**
     * 联系人
     */
    @Column(name = "contact_person")
    private String contactPerson;

    /**
     * 邮箱
     */
    private String mailbox;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
