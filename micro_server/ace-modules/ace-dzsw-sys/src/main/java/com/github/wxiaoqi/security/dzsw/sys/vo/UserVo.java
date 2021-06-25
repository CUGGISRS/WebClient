package com.github.wxiaoqi.security.dzsw.sys.vo;

import com.github.wxiaoqi.security.dzsw.sys.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("用户注册实体")
@Data
public class UserVo extends User {

    /**
     * 账号
     */
    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 类型
     */
    @ApiModelProperty("注册,传2")
    private Integer smsType;
}
