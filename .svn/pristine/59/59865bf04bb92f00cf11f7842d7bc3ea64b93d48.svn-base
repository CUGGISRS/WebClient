package com.github.wxiaoqi.security.dzsw.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 王军伟
 * @date 2020/5/20 下午7:34
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("短信发送实体")
public class SendSmsDto implements Serializable {
    private static final long serialVersionUID = 68620056131350971L;

    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("发送类型；注册默认传2 ,REGIST")
    private int type;
}
