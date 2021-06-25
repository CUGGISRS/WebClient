package com.github.wxiaoqi.security.dzsw.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@ApiModel("用户实体")
@Table(name = "com_sys_user_info")
public class User {
    /**
     * 用户ID
     */
//    @JsonIgnore
    @Id
    private Integer id;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
//    @NotBlank(message = "姓名不能为空")
//    @Size(min = 1, max = 10, message = "姓名最少1位，最高10位")
    private String name;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账户最少3位，最高20位")
    private String account;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
//    @NotBlank(message = "名称不能为空")
//    @Size(min = 1, max = 20, message = "名称最少1位，最高20位")
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 是否启用（0否 1是）
     */
//    @JsonIgnore
    private String status;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 3, max = 20, message = "密码最少5位，最高20位")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Size(min = 5, max = 12, message = "手机号格式错误")
    private String phone;

    /**
     * 目标系统
     */
//    @JsonIgnore
    @Column(name = "target_system")
    private String targetSystem;

    /**
     * 是否认证 (0未 1已)认证
     */
//    @JsonIgnore
    @Column(name = "is_verify")
    private Integer isVerify;

    /**
     * 头像
     */
//    @JsonIgnore
    private String photo;

    /**
     * 注册时间
     */
//    @JsonIgnore
    @Column(name = "reg_time")
    private Date regTime;

    /**
     * 是否删除
     */
//    @JsonIgnore
    @Column(name = "is_deleted")
    private Integer isDeleted;

    public User() {
    }

    public User(Integer id, String photo) {
        this.id = id;
        this.photo = photo;
    }

    /**
     * 获取用户ID
     *
     * @return id - 用户ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户ID
     *
     * @param id 用户ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取账号
     *
     * @return account - 账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号
     *
     * @param account 账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取名称
     *
     * @return sys_name - 名称
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 设置名称
     *
     * @param sysName 名称
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * 获取是否启用
     *
     * @return status - 是否启用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置是否启用
     *
     * @param status 是否启用
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取目标系统
     *
     * @return target_system - 目标系统
     */
    public String getTargetSystem() {
        return targetSystem;
    }

    /**
     * 设置目标系统
     *
     * @param targetSystem 目标系统
     */
    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    /**
     * 获取是否认证 (0未 1已)认证
     *
     * @return is_verify - 是否认证 (0未 1已)认证
     */
    public Integer getIsVerify() {
        return isVerify;
    }

    /**
     * 设置是否认证 (0未 1已)认证
     *
     * @param isVerify 是否认证 (0未 1已)认证
     */
    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    /**
     * 获取头像
     *
     * @return photo - 头像
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置头像
     *
     * @param photo 头像
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取注册时间
     *
     * @return reg_time - 注册时间
     */
    public Date getRegTime() {
        return regTime;
    }

    /**
     * 设置注册时间
     *
     * @param regTime 注册时间
     */
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
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
