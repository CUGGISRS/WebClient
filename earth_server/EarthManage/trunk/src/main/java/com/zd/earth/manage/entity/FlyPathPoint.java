package com.zd.earth.manage.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
/**
 * 飞行路径点
 */
@Table(name = "fly_path_point")
@Data
public class FlyPathPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 飞行路径id
     */
    @Column(name = "fly_path_id")
    private Integer flyPathId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型（1飞行点，2观察点，3环绕点）
     */
    private Integer type;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 高程(米)
     */
    private BigDecimal altitude;

    /**
     * 高度(米)
     */
    private BigDecimal height;

    /**
     * 朝向角度（度）
     */
    @Column(name = "orientation_angle")
    private BigDecimal orientationAngle;

    /**
     * 俯仰角度（度）
     */
    @Column(name = "pitch_angle")
    private BigDecimal pitchAngle;

    /**
     * 旋转角度(度)
     */
    @Column(name = "rotate_angle")
    private BigDecimal rotateAngle;

    /**
     * 停滞时间(秒)
     */
    @Column(name = "dead_time")
    private BigDecimal deadTime;

    /**
     * 速度(米/秒)
     */
    private BigDecimal speed;

    /**
     * 半径(米)
     */
    private BigDecimal radius;

    /**
     * 圈数(圈)
     */
    @Column(name = "circle_num")
    private BigDecimal circleNum;
    @Transient
    private List<FlyPathPointSingle> flyPathPointSingles;
}
