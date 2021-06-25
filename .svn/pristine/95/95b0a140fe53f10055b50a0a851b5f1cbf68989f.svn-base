package com.github.wxiaoqi.security.jd.sys.entity;

import java.math.BigDecimal;
import javax.persistence.*;

import lombok.Data;

/**
 * 预警阈值
 */
@Table(name = "aqjd_warning_threshold")
@Data
public class WarningThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 预警阈值
     */
    private BigDecimal threshold;
}
