package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物联网传感器报警阈值
 */
@Table(name = "xph.xph_warning_threshold")
@Data
@NoArgsConstructor
public class WarningThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 传感器id
     */
    @Column(name = "sensor_id")
    private Integer sensorId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 最大值
     */
    @Column(name = "max_value")
    private BigDecimal maxValue;

    /**
     * 最小值
     */
    @Column(name = "min_value")
    private BigDecimal minValue;

    /**
     * 报警信息
     */
    @Column(name = "warning_info")
    private String warningInfo;

    /**
     * 是否发送短信(1是，0否)
     */
    @Column(name = "is_send")
    private Integer isSend;

    /**
     * 企业物联网管理员id
     */
    @Column(name = "enterprise_wl_manager_id")
    private Integer enterpriseWLManagerId;


    /**
     * 发送短信间隔（分钟,默认15）
     */
    @Column(name = "send_interval")
    private Integer sendInterval;

    /**
     * 最后发送短信时间
     */
    @Column(name = "last_record_time")
    private Date lastRecordTime;

    public WarningThreshold(Integer id, Date lastRecordTime) {
        this.id = id;
        this.lastRecordTime = lastRecordTime;
    }
}
