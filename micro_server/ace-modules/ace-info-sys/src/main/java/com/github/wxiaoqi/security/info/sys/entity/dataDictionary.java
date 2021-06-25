package com.github.wxiaoqi.security.info.sys.entity;

import javax.persistence.*;

@Table(name = "data_dictionary")
public class dataDictionary {
    /**
     * 编号
     */
    @Id
    private String code;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 数据值
     */
    private String text;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取编号
     *
     * @return code - 编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code 编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取字典类型
     *
     * @return type - 字典类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置字典类型
     *
     * @param type 字典类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取数据值
     *
     * @return text - 数据值
     */
    public String getText() {
        return text;
    }

    /**
     * 设置数据值
     *
     * @param text 数据值
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
