package com.lomoye.easy.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @description: 导出表的参数
 * @author: gsy
 * @create: 2020-10-22 11:43
 **/
@Data
//@ApiModel
public class ExportParams {

    @ApiModelProperty("获取数据的表名")
    private String tableName;

    @ApiModelProperty("系统类型")
    private String sysType;

    @ApiModelProperty("文章类型")
    private String articleType;

}
