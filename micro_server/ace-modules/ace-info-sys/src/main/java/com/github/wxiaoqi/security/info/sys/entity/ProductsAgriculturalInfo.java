package com.github.wxiaoqi.security.info.sys.entity;

import javax.persistence.*;

@Table(name = "zsxx_products_agricultural_info")
public class ProductsAgriculturalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 农产品名称
     */
    private String name;

    /**
     * 总类
     */
    @Column(name = "all_type")
    private String allType;

    /**
     * 类别
     */
    private String type;

    /**
     * 图片
     */
    private Integer img;

    /**
     * 描述
     */
    private String description;

    /**
     * 企业id
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 三品一标id
     */
    @Column(name = "auth_id")
    private Integer authId;

    /**
     * 是否已删除
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
     * 获取农产品名称
     *
     * @return name - 农产品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置农产品名称
     *
     * @param name 农产品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取总类
     *
     * @return all_type - 总类
     */
    public String getAllType() {
        return allType;
    }

    /**
     * 设置总类
     *
     * @param allType 总类
     */
    public void setAllType(String allType) {
        this.allType = allType;
    }

    /**
     * 获取类别
     *
     * @return type - 类别
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类别
     *
     * @param type 类别
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取图片
     *
     * @return img - 图片
     */
    public Integer getImg() {
        return img;
    }

    /**
     * 设置图片
     *
     * @param img 图片
     */
    public void setImg(Integer img) {
        this.img = img;
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
     * 获取企业id
     *
     * @return company_id - 企业id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 设置企业id
     *
     * @param companyId 企业id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取三品一标id
     *
     * @return auth_id - 三品一标id
     */
    public Integer getAuthId() {
        return authId;
    }

    /**
     * 设置三品一标id
     *
     * @param authId 三品一标id
     */
    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    /**
     * 获取是否已删除
     *
     * @return is_deleted - 是否已删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否已删除
     *
     * @param isDeleted 是否已删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}