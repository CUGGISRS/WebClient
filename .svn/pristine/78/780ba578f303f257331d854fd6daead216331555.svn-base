package com.github.wxiaoqi.security.consultation.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dt_expertduty")
public class DtExpertduty {
    /**
     * 排班编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * 排班日期
     */
    @Column(name = "Date")
    private Date date;

    /**
     * 专家ID
     */
    @Column(name = "ExpertID")
    private Integer expertid;

    /**
     * 专家姓名
     */
    @Column(name = "ExpertName")
    private String expertname;

    /**
     * 获取排班编号
     *
     * @return ID - 排班编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置排班编号
     *
     * @param id 排班编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取排班日期
     *
     * @return Date - 排班日期
     */
    public Date getDate() {
        return date;
    }

    /**
     * 设置排班日期
     *
     * @param date 排班日期
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取专家ID
     *
     * @return ExpertID - 专家ID
     */
    public Integer getExpertid() {
        return expertid;
    }

    /**
     * 设置专家ID
     *
     * @param expertid 专家ID
     */
    public void setExpertid(Integer expertid) {
        this.expertid = expertid;
    }

    /**
     * 获取专家姓名
     *
     * @return ExpertName - 专家姓名
     */
    public String getExpertname() {
        return expertname;
    }

    /**
     * 设置专家姓名
     *
     * @param expertname 专家姓名
     */
    public void setExpertname(String expertname) {
        this.expertname = expertname;
    }
}
