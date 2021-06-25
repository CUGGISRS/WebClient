package com.github.wxiaoqi.security.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 */
@Data
@NoArgsConstructor
public class UserInfo{
    private  Integer id;
    private  String name;
    private Integer type;
    private String username;
    private String sysName;

    private Integer enterpriseId;
    private Integer isViewpoint;

    public UserInfo(Integer enterpriseId, Integer isViewpoint) {
        this.enterpriseId = enterpriseId;
        this.isViewpoint = isViewpoint;
    }
}
