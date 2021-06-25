package com.github.wxiaoqi.security.gyx.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table(name = "com_file_info")
public class FileInfo {
    /**
     * 文件信息表id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联表
     */
    @JsonIgnore
    @Column(name = "link_table")
    private String linkTable;

    /**
     * 关联表ID
     */
    @Column(name = "table_id")
    private Integer tableId;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 是否删除
     */
    @JsonIgnore
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 获取文件信息表id
     *
     * @return id - 文件信息表id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置文件信息表id
     *
     * @param id 文件信息表id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取关联表
     *
     * @return link_table - 关联表
     */
    public String getLinkTable() {
        return linkTable;
    }

    /**
     * 设置关联表
     *
     * @param linkTable 关联表
     */
    public void setLinkTable(String linkTable) {
        this.linkTable = linkTable;
    }

    /**
     * 获取关联表ID
     *
     * @return table_id - 关联表ID
     */
    public Integer getTableId() {
        return tableId;
    }

    /**
     * 设置关联表ID
     *
     * @param tableId 关联表ID
     */
    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    /**
     * 获取文件路径
     *
     * @return path - 文件路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置文件路径
     *
     * @param path 文件路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
