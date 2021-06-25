package com.github.wxiaoqi.security.gyx.sys.dto;

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
public class SendSmsDto implements Serializable {
    private static final long serialVersionUID = 68620056131350971L;

    private String mobile;
    private int type;
}
