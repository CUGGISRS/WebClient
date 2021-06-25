package com.github.wxiaoqi.security.jd.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预警信息
 */
@Table(name = "aqjd_warning_info")
@Data
@NoArgsConstructor
public class WarningInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 预警时间
     */
    private Date date;

    /**
     * 类型(1水产，2种植)
     */
    private Integer type;

    /**
     * 状态(0未发布，1已发布)
     */
    private Integer status;
    /**
     * 预警内容
     */
    private String content;

    public WarningInfo(Date date, Integer type, String content) {
        this.date = date;
        this.type = type;
        this.content = content;
    }

    public WarningInfo(Date date, Integer type, Integer status, String content) {
        this.date = date;
        this.type = type;
        this.status = status;
        this.content = content;
    }
}
