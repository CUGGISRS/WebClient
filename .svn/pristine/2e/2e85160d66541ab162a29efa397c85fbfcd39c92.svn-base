package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 物联网设备传感器
 */
@Table(name = "xph.xph_device_sensor")
@Data
@NoArgsConstructor
public class DeviceSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 传感器编号
     */
    private String number;

    /**
     * 传感器名称
     */
    private String name;

    /**
     * 传感器类型(1要素，2继电器)
     */
    private Integer type;

    /**
     * 设备ID号
     */
    @Column(name = "fac_id")
    private Integer facId;

    /**
     * 设备通道
     */
    @Column(name = "fac_pass")
    private String facPass;


    /**
     * 分组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 传感器单位
     */
    private String unit;
    /**
     * 传感器分辨率
     */
    @Column(name = "pre_c")
    private String preC;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;


    /**
     * 企业物联网用户表id
     */
    @Column(name = "enterprise_wl_user_id")
    private Integer enterpriseWlUserId;

    /**
     * 最后一次同步时的记录时间
     */
    @Column(name = "last_record_time")
    private Date lastRecordTime;

    /**
     * 最后一次同步时的记录数据
     */
    @Column(name = "last_record_data")
    private BigDecimal lastRecordData;
    @Transient
    List<DeviceSensorData> dsdList;
    @Transient
    List<WarningThreshold> wtList;

    public DeviceSensor(Integer id, Date lastRecordTime, BigDecimal lastRecordData) {
        this.id = id;
        this.lastRecordTime = lastRecordTime;
        this.lastRecordData = lastRecordData;
    }
}
