package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物联网传感器报警信息
 */
@Table(name = "xph.xph_warning_info")
@Data
@NoArgsConstructor
public class WarningInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 传感器id
     */
    @Column(name = "sensor_id")
    private Integer sensorId;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 报警时间
     */
    @Column(name = "warning_date")
    private Date warningDate;

    /**
     * 报警级别
     */
    @Column(name = "warning_level")
    private Integer warningLevel;

    /**
     * 报警信息
     */
    @Column(name = "warning_info")
    private String warningInfo;

    /**
     * 状态（0未读,1已读）
     */
    private Integer status;

    /**
     * 报警数据
     */
    @Column(name = "warning_data")
    private BigDecimal warningData;

    public WarningInfo(Integer sensorId, Integer enterpriseId, Date warningDate,
                       Integer warningLevel, String warningInfo, Integer status, BigDecimal warningData) {
        this.sensorId = sensorId;
        this.enterpriseId = enterpriseId;
        this.warningDate = warningDate;
        this.warningLevel = warningLevel;
        this.warningInfo = warningInfo;
        this.status = status;
        this.warningData = warningData;
    }
}
