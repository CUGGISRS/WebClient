package com.zd.earth.manage.entity;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞行路径点单点
 */
@Table(name = "fly_path_point_single")
@Data
@NoArgsConstructor
public class FlyPathPointSingle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 序号
     */
    @Column(name = "index_num")
    private Integer indexNum;

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
     * 飞行路径点id
     */
    @Column(name = "path_point_id")
    private Integer pathPointId;

    public FlyPathPointSingle(BigDecimal height) {
        this.height = height;
    }
}
