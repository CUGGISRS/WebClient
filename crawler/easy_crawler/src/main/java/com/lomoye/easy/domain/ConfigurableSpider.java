package com.lomoye.easy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lomoye.easy.model.ListPatternModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

/**
 * 2019/9/10 14:58
 * yechangjun
 */
@Data
public class ConfigurableSpider extends CommonDomain {

    @ApiModelProperty(notes = "爬虫名")
    private String name;

    @ApiModelProperty(notes = "存储的表名", required = true)
    private String tableName;

    @ApiModelProperty(notes = "列表页正则表达式", required = true)
    private String listRegex;

    @ApiModelProperty(notes = "入口页", required = true)
    private String entryUrl;

    //后添加的字段----------------------------------------------------------
    @ApiModelProperty(notes = "作者")
    private String author;

    @ApiModelProperty(notes = "需要处理时间的正则,与time一起使用，将time处理为正确格式")
    private String timeRegex;

    @ApiModelProperty(notes = "正文页待拼接的url,与正文页xpath一起使用")
    private String contentUrl;
    //----------------------------------------------------------

    @ApiModelProperty(notes = "正文页xpath")
    private String contentXpath;

    @ApiModelProperty(notes = "列表页字段规则json字符串")
    private String fieldsJson;

    @ApiModelProperty(notes = "正文页规则json字符串")
    private String contentFieldsJson;

    //爬虫配置项
    @ApiModelProperty(notes = "线程数")
    private Integer threadNum;

    @ApiModelProperty(notes = "每个页面处理完后的睡眠时间 单位秒")
    private Integer sleepTime;

    @ApiModelProperty(notes = "页面下载失败重试次数")
    private Integer retryTimes;

    @ApiModelProperty(notes = "重试睡眠时间 单位秒")
    private Integer retrySleepTime;

    @ApiModelProperty(notes = "页面爬取失败后放回队列的次数")
    private Integer cycleRetryTimes;

    @ApiModelProperty(notes = "下载页面超时时间 单位秒")
    private Integer timeOut;

    //代理配置
    @ApiModelProperty(notes = "代理id")
    private String proxyChannelId;

    @TableField(exist = false)
    private LinkedHashMap<String/*字段名*/, String/*xpath*/> fields;

    @TableField(exist = false)
    private LinkedHashMap<String/*字段名*/, String/*xpath*/> contentFields;

    public static ConfigurableSpider valueOf(ListPatternModel model) {
        ConfigurableSpider spider = new ConfigurableSpider();

        spider.setName(model.getName());
        spider.setTableName(model.getTableName());
        spider.setListRegex(model.getListRegex());
        spider.setEntryUrl(model.getEntryUrl());
        spider.setFieldsJson(model.getFieldsJson());
        spider.setContentXpath(model.getContentXpath());

        //新增作者
        spider.setAuthor(model.getAuthor());
        //新增 contentUrl
        spider.setContentUrl(model.getContentUrl());
        //新增 timeRegex
        spider.setTimeRegex(model.getTimeRegex());


        spider.setContentFieldsJson(model.getContentFieldsJson());

        spider.setThreadNum(1);
        spider.setSleepTime(5);
        spider.setRetryTimes(0);
        spider.setRetrySleepTime(1);
        spider.setCycleRetryTimes(0);
        spider.setTimeOut(5);

        spider.setCreateTime(LocalDateTime.now());
        spider.setModifyTime(LocalDateTime.now());

        return spider;
    }
}
