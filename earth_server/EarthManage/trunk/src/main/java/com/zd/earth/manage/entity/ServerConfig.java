package com.zd.earth.manage.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务配置
 */
@Table(name = "server_config")
@Data
@NoArgsConstructor
public class ServerConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 配置名称(唯一)
     */
    @Column(name = "config_name")
    private String configName;

    /**
     * 配置内容
     */
    @Column(name = "config_content")
    private String configContent;

    public ServerConfig(String configName) {
        this.configName = configName;
    }
}
