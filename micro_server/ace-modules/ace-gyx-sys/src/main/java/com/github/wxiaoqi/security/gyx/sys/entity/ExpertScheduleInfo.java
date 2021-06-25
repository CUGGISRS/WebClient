package com.github.wxiaoqi.security.gyx.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "gyx_expert_schedule_info")
public class ExpertScheduleInfo {
    @Id
    private Integer id;

    /**
     * 排班日期
     */
    @Column(name = "schedule_time")
    private Date scheduleTime;

    /**
     * 专家ID
     */
    @Column(name = "expert_ID")
    private Integer expertId;

    /**
     * 专家名称
     */
    @Column(name = "expert_name")
    private String expertName;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取排班日期
     *
     * @return schedule_time - 排班日期
     */
    public Date getScheduleTime() {
        return scheduleTime;
    }

    /**
     * 设置排班日期
     *
     * @param scheduleTime 排班日期
     */
    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * 获取专家ID
     *
     * @return expert_ID - 专家ID
     */
    public Integer getExpertId() {
        return expertId;
    }

    /**
     * 设置专家ID
     *
     * @param expertId 专家ID
     */
    public void setExpertId(Integer expertId) {
        this.expertId = expertId;
    }

    /**
     * 获取专家名称
     *
     * @return expert_name - 专家名称
     */
    public String getExpertName() {
        return expertName;
    }

    /**
     * 设置专家名称
     *
     * @param expertName 专家名称
     */
    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}