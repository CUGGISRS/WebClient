package com.zd.earth.manage.entity;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * 飞行路径
 */
@Table(name = "fly_path")
@Data
public class FlyPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;
    @Transient
    private List<FlyPathPoint> flyPathPoints;
}
