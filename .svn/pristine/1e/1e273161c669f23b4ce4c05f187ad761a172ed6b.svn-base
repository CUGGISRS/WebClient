package com.zd.earth.manage.entity;

import javax.persistence.*;
import lombok.Data;

/**
 * 本地代理
 */
@Table(name = "local_agent")
@Data
public class LocalAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 本地路径
     */
    private String path;

    /**
     * 标题
     */
    private String title;
}
