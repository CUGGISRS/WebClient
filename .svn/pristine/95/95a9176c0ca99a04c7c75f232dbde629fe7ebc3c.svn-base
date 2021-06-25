package com.github.wxiaoqi.security.zs.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;

/**
 * 物联网设备分组
 */
@Table(name = "xph.xph_device_group")
@Data
public class DeviceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 组名
     */
    private String name;

    /**
     * 创建者ID
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
