package com.github.wxiaoqi.security.info.sys.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "文章系统实体")
public class ArticleAndSystem extends ArticleInfo {

    @ApiModelProperty("文章id，新增不传，修改需要传")
    private Integer id;

    @ApiModelProperty("系统类型")
    private String[] systemId;

    @ApiModelProperty("文章类型")
    private String articleTypeid;

    @ApiModelProperty("发布状态 (0未发布，1已发布) ")
    @NotBlank(message = "articleStatus不能为空")
    private Integer articleStatus;

}
