package com.github.wxiaoqi.security.consultation.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Table(name = "bi_expert")
public class BiExpert {
//    /**
//     * 平均得分
//     */
//    @Transient
//    private double averScore;
//
//    public double getAverScore() {
//        return averScore;
//    }
//
//    public void setAverScore(double averScore) {
//        this.averScore = averScore;
//    }
//    添加User属性

//    @Transient
//    private User user;
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//    @Override
//    public String toString() {
//        return "BiExpert{" +
//                "user=" + user +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", id=" + id +
//                ", name='" + name + '\'' +
//                ", business='" + business + '\'' +
//                ", official='" + official + '\'' +
//                ", location='" + location + '\'' +
//                ", telephone='" + telephone + '\'' +
//                ", wechat='" + wechat + '\'' +
//                ", introduction='" + introduction + '\'' +
//                ", image=" + Arrays.toString(image) +
//                '}';
//    }

    /**
     * 用户名
     */
    @Transient
    private String username;

    /**
     * 获取用户名
     *
     * @return Username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 密码
     */
    @Transient
    private String password;

    /**
     * 获取用户名
     *
     * @return Password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户名
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 专家编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * 专家姓名
     */
    @Column(name = "Name")
    private String name;

    /**
     * 业务专长
     */
    @Column(name = "Business")
    private String business;

    /**
     * 职务职称
     */
    @Column(name = "Official")
    private String official;

    /**
     * 服务区县
     */
    @Column(name = "Location")
    private String location;

    /**
     * 联系方式
     */
    @Column(name = "Telephone")
    private String telephone;

    /**
     * 微信
     */
    @Column(name = "Wechat")
    private String wechat;

    /**
     * 介绍
     */
    @Column(name = "Introduction")
    private String introduction;

    /**
     * 照片
     */
    @Column(name = "Image")
    private byte[] image;

    /**
     * 获取专家编号
     *
     * @return ID - 专家编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置专家编号
     *
     * @param id 专家编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取专家姓名
     *
     * @return Name - 专家姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置专家姓名
     *
     * @param name 专家姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取业务专长
     *
     * @return Business - 业务专长
     */
    public String getBusiness() {
        return business;
    }

    /**
     * 设置业务专长
     *
     * @param business 业务专长
     */
    public void setBusiness(String business) {
        this.business = business;
    }

    /**
     * 获取职务职称
     *
     * @return Official - 职务职称
     */
    public String getOfficial() {
        return official;
    }

    /**
     * 设置职务职称
     *
     * @param official 职务职称
     */
    public void setOfficial(String official) {
        this.official = official;
    }

    /**
     * 获取服务区县
     *
     * @return Location - 服务区县
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置服务区县
     *
     * @param location 服务区县
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取联系方式
     *
     * @return Telephone - 联系方式
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置联系方式
     *
     * @param telephone 联系方式
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取微信
     *
     * @return Wechat - 微信
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * 设置微信
     *
     * @param wechat 微信
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * 获取介绍
     *
     * @return Introduction - 介绍
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置介绍
     *
     * @param introduction 介绍
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * 获取照片
     *
     * @return Image - 照片
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * 设置照片
     *
     * @param image 照片
     */
    public void setImage(byte[] image) {
        this.image = image;
    }
}
