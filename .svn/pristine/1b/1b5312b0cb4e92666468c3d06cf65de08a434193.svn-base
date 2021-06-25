package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物联网设备传感器数据
 */
@Table(name = "xph.xph_device_sensor_data")
@Data
@NoArgsConstructor
public class DeviceSensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 传感器id
     */
    @Column(name = "sensor_id")
    private Integer sensorId;

    /**
     * 记录时间
     */
    @Column(name = "record_time")
    private Date recordTime;

    /**
     * 记录数据
     */
    @Column(name = "record_data")
    private BigDecimal recordData;

    /**
     * 同步时间
     */
    @Column(name = "sync_time")
    private Date syncTime;


    public DeviceSensorData(Integer sensorId, Date recordTime, BigDecimal recordData, Date syncTime) {
        this.sensorId = sensorId;
        this.recordTime = recordTime;
        this.recordData = recordData;
        this.syncTime = syncTime;
    }
}
