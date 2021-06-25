package com.zd.earth.manage.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图形数据
 */
@Table(name = "figure_data")
@Data
@NoArgsConstructor
public class FigureData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 内容（json字符串）
     */
    private String content;

    public FigureData(String name) {
        this.name = name;
    }
}
