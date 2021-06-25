package com.github.wxiaoqi.security.gyx.sys.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "gyx_supply_demand_info")
public class SupplyDemandInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 联系人
     */
    @Column(name = "contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 所在地区
     */
    private String area;

    /**
     * 图片
     */
    private Integer picture;

    /**
     * 描述
     */
    private String description;

    /**
     * 发布日期
     */
    @Column(name = "pub_time")
    private Date pubTime;

    /**
     * 发布类型
     */
    @Column(name = "pub_type")
    private String pubType;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 用户id
     */
    private Integer userid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取联系人
     *
     * @return contact_person - 联系人
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * 设置联系人
     *
     * @param contactPerson 联系人
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * 获取联系电话
     *
     * @return phone - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取所在地区
     *
     * @return area - 所在地区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置所在地区
     *
     * @param area 所在地区
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取图片
     *
     * @return picture - 图片
     */
    public Integer getPicture() {
        return picture;
    }

    /**
     * 设置图片
     *
     * @param picture 图片
     */
    public void setPicture(Integer picture) {
        this.picture = picture;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取发布日期
     *
     * @return pub_time - 发布日期
     */
    public Date getPubTime() {
        return pubTime;
    }

    /**
     * 设置发布日期
     *
     * @param pubTime 发布日期
     */
    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    /**
     * 获取发布类型
     *
     * @return pub_type - 发布类型
     */
    public String getPubType() {
        return pubType;
    }

    /**
     * 设置发布类型
     *
     * @param pubType 发布类型
     */
    public void setPubType(String pubType) {
        this.pubType = pubType;
    }

    /**
     * 获取审核状态
     *
     * @return status - 审核状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置审核状态
     *
     * @param status 审核状态
     */
    public void setStatus(String status) {
        this.status = status;
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
