package com.zd.earth.manage.entity;

import javax.persistence.*;
import lombok.Data;

/**
 * 服务状态
 */
@Table(name = "server_status")
@Data
public class ServerStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String uri;

    /**
     * 状态(1启动，0停止)
     */
    private Integer status;
}
