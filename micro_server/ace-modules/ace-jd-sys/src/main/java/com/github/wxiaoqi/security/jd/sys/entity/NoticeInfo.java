package com.github.wxiaoqi.security.jd.sys.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;

/**
 * 公告信息
 */
@Table(name = "aqjd_notice_info")
@Data
public class NoticeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private Date date;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型（1水产，2种植）
     */
    private Integer type;

    /**
     * 状态(0未发布，1已发布)
     */
    private Integer status;

    /**
     * 创建者名称
     */
    @Column(name = "creator_name")
    private String creatorName;

    /**
     * 创建者用户id
     */
    @Column(name = "creator_user_id")
    private Integer creatorUserId;
}
