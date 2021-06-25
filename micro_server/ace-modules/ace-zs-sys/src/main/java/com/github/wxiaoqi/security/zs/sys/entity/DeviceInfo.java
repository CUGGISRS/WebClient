package com.github.wxiaoqi.security.zs.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;

/**
 * 物联网设备
 */
@Table(name = "xph.xph_device")
@Data
public class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 设备ID号
     */
    @Column(name = "fac_id")
    private Integer facId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 设备名称
     */
    @Column(name = "fac_name")
    private String facName;

    /**
     * 设备类型
     */
    @Column(name = "fac_type")
    private Integer facType;

    /**
     * 要素配置索引
     */
    @Column(name = "ele_num")
    private String eleNum;

    /**
     * 要素配置名称
     */
    @Column(name = "ele_name")
    private String eleName;

    /**
     * 继电器配置索引
     */
    @Column(name = "relay_num")
    private String relayNum;

    /**
     * 继电器配置名称
     */
    @Column(name = "relay_name")
    private String relayName;

    /**
     * 经度
     */
    private Float longitude;

    /**
     * 纬度
     */
    private Float latitude;

    /**
     * 读取实时数据间隔，单位是分钟
     */
    @Column(name = "read_interval")
    private Integer readInterval;

    /**
     * 硬件地址
     */
    private Integer address;

    /**
     * 是否有串口摄像头
     */
    private Boolean photo;

    /**
     * 创建者ID
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 是否继电器扩展
     */
    @Column(name = "relay_extend")
    private Boolean relayExtend;

    /**
     * 继电器扩展的数量，不包含默认的32路
     */
    @Column(name = "relay_extend_count")
    private Integer relayExtendCount;

    /**
     * 继电器扩展序号
     */
    @Column(name = "relay_extend_num")
    private String relayExtendNum;

    /**
     * 继电器扩展名称
     */
    @Column(name = "relay_extend_name")
    private String relayExtendName;

    /**
     * 设备封面
     */
    @Column(name = "cover_url")
    private String coverUrl;

    /**
     * 虫情IMEI
     */
    @Column(name = "pest_imei")
    private String pestImei;
}
