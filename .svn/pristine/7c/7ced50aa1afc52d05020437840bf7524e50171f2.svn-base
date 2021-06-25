package com.lomoye.easy.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2019/8/28 14:56
 * yechangjun
 * 列表模式
 */
@Data
public class ListPatternModel {
    @ApiModelProperty(notes = "爬虫名", required = true)
    private String name;

    @ApiModelProperty(notes = "列表页正则表达式", required = true)
    private String listRegex;

    @ApiModelProperty(notes = "入口页", required = true)
    private String entryUrl;

    @ApiModelProperty(notes = "存储的表名", required = true)
    private String tableName;

    @ApiModelProperty(notes = "入口页字段规则json字符串")
    private String fieldsJson;

    //--------------------------------------------------------------------
    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty(notes = "时间正则")
    private String timeRegex;

    @ApiModelProperty(notes = "正文页待拼接的url,与正文页xpath一起使用")
    private String contentUrl;
    //----------------------------------------------------------------------

    @ApiModelProperty(notes = "正文页xpath")
    private String contentXpath;

    @ApiModelProperty(notes = "正文页规则json字符串")
    private String contentFieldsJson;
}
