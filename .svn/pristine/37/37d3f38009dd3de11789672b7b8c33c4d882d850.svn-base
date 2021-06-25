package com.github.wxiaoqi.security.com.sys.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件
 */
@Table(name = "com_file")
@Data
@NoArgsConstructor
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 表id
     */
    @Column(name = "form_id")
    private Integer formId;

    /**
     * 表名
     */
    @Column(name = "form_name")
    private String formName;

    /**
     * 表文件类型
     */
    private Integer type;

    /**
     * 路径
     */
    private String url;

    public FileInfo(Integer formId, String formName, Integer type, String url) {
        this.formId = formId;
        this.formName = formName;
        this.type = type;
        this.url = url;
    }
}
