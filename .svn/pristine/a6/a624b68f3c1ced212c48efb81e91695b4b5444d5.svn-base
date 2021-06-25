package com.github.wxiaoqi.security.info.sys.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@ApiModel(value = "三品一标认证表")
@Table(name = "zsxx_auth_info")
public class AuthInfo {
    @Id
    //标注为自增id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty("认证名称")
    @Column(name = "`auth _name`")
    private String authName;

    /**
     * 证书编号
     */
    private String number;

    /**
     * 颁证机构
     */
    private String institution;

    /**
     * 认证日期
     */
    @Column(name = "auth_date")
    private String authDate;

    /**
     * 过期时间
     */
    @Column(name = "past_date")
    private String pastDate;

    /**
     * 证书图片
     */
    @Column(name = "auth_img")
    private Integer authImg;

    /**
     * 农产品id
     */
    @Column(name = "products_id")
    private Integer productsId;

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
     * 获取名称
     *
     * @return auth _name - 名称
     */
    public String getAuthName() {
        return authName;
    }

    /**
     * 设置名称
     *
     * @param authName 名称
     */
    public void setAuthName(String authName) {
        this.authName = authName;
    }

    /**
     * 获取证书编号
     *
     * @return number - 证书编号
     */
    public String getNumber() {
        return number;
    }

    /**
     * 设置证书编号
     *
     * @param number 证书编号
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取颁证机构
     *
     * @return institution - 颁证机构
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * 设置颁证机构
     *
     * @param institution 颁证机构
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * 获取认证日期
     *
     * @return auth_date - 认证日期
     */
    public String getAuthDate() {
        return authDate;
    }

    /**
     * 设置认证日期
     *
     * @param authDate 认证日期
     */
    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }

    /**
     * 获取过期时间
     *
     * @return past_date - 过期时间
     */
    public String getPastDate() {
        return pastDate;
    }

    /**
     * 设置过期时间
     *
     * @param pastDate 过期时间
     */
    public void setPastDate(String pastDate) {
        this.pastDate = pastDate;
    }

    /**
     * 获取证书图片
     *
     * @return auth_img - 证书图片
     */
    public Integer getAuthImg() {
        return authImg;
    }

    /**
     * 设置证书图片
     *
     * @param authImg 证书图片
     */
    public void setAuthImg(Integer authImg) {
        this.authImg = authImg;
    }

    /**
     * 获取农产品id
     *
     * @return products_id - 农产品id
     */
    public Integer getProductsId() {
        return productsId;
    }

    /**
     * 设置农产品id
     *
     * @param productsId 农产品id
     */
    public void setProductsId(Integer productsId) {
        this.productsId = productsId;
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