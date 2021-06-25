package com.zd.earth.manage.entity;

import java.math.BigDecimal;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视点管理
 */
@Table(name = "view_manage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 起点(20,3)
     */
    private BigDecimal heading;

    /**
     * 最高点(20,3)
     */
    private BigDecimal pitch;

    /**
     * 翻转点(20,3)
     */
    private BigDecimal roll;

    /**
     * 观察值
     */
    @Column(name = "watch_value")
    private String watchValue;

    /**
     * 高度(20,3)
     */
    private BigDecimal height;

    /**
     * 经度(10,7)
     */
    private BigDecimal longitude;

    /**
     * 纬度(10,7)
     */
    private BigDecimal latitude;

}
