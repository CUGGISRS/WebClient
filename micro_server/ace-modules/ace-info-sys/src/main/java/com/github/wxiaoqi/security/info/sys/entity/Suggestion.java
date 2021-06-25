package com.github.wxiaoqi.security.info.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "zsxx_suggestion")
@Data
public class Suggestion {
    @Id
    private Integer id;

    /**
     * 留言类型 投诉&建议
     */
    private String type;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 名字
     */
    @ApiModelProperty("名字")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 1 已处理 0未处理
     */
    private Integer status;

    /**
     * 1 已删除 0未删除
     */
    @JsonIgnore
    @Column(name = "id_deleted")
    private Integer idDeleted;

    @Column(name = "updName")
    private String updName;

    /**
     * 修改时间
     */
    @Column(name = "upd_time")
    private Date updTime;
}
