package com.github.wxiaoqi.security.dzsw.sys.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "data_dictionary")
public class DataDictionaryInfo {
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


    public DataDictionaryInfo(String code, String type, String text, String remark) {
        this.code = code;
        this.type = type;
        this.text = text;
        this.remark = remark;
    }
}
