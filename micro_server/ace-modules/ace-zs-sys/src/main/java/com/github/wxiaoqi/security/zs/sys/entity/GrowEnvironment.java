package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 生长环境
 */
@Table(name = "aqzs_grow_environment")
@Data
@NoArgsConstructor
public class GrowEnvironment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 生长环境名称
     */
    private String name;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 设备传感器id
     */
    @Column(name = "device_sensor_id")
    private Integer deviceSensorId;
    @Transient
    private DeviceSensor deviceSensor;

    public GrowEnvironment(String productName) {
        this.productName = productName;
    }
}
