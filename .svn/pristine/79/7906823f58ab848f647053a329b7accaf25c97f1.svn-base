package com.github.wxiaoqi.security.zs.sys.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.entity.User;
import lombok.Data;

/**
 * 企业
 */
@Table(name = "aqzs_enterprise")
@Data
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 经营类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 统一社会信用代码
     */
    @Column(name = "social_code")
    private String socialCode;

    /**
     * 行政区规划
     */
    private String district;

    /**
     * 组织形式
     */
    @Column(name = "organization_form")
    private String organizationForm;

    /**
     * 主体属性
     */
    @Column(name = "subject_attribute")
    private String subjectAttribute;

    /**
     * 注册地址
     */
    @Column(name = "register_address")
    private String registerAddress;

    /**
     * 成立日期
     */
    @Column(name = "establish_date")
    private Date establishDate;

    /**
     * 注册资金（万元）
     */
    @Column(name = "register_money")
    private BigDecimal registerMoney;

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
     * 邮箱
     */
    private String mailbox;

    /**
     * 邮政编码
     */
    private String postcode;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 法定代表人姓名
     */
    @Column(name = "deputy_name")
    private String deputyName;

    /**
     * 法定代表人身份证号
     */
    @Column(name = "deputy_id_card")
    private String deputyIdCard;

    /**
     * 法定代表人联系电话
     */
    @Column(name = "deputy_phone")
    private String deputyPhone;

    /**
     * 信用等级(星级分为1-5)
     */
    @Column(name = "credit_level")
    private Integer creditLevel;
    /**
     * 状态(0待审核，1正常)
     */
    private Integer status;


    /**
     * 物联网用户id
     */
    @Column(name = "w_user_id")
    private Integer wlUserId;

    /**
     * 物联网用户名
     */
    @Column(name = "w_username")
    private String wlUsername;
    /**
     * 物联网密码
     */
    @Column(name = "w_password")
    private String wlPassword;

    /**
     * 是否为视点（1是，0否）
     */
    @Column(name = "is_viewpoint")
    private Integer isViewpoint;

    /**
     * 评价等级（优、良、中、差）
     */
    @Column(name = "evaluate_grade")
    private String evaluateGrade;

    /**
     * 评价时间
     */
    @Column(name = "evaluate_time")
    private Date evaluateTime;


    /**
     * 营业执照图片
     */
    @Transient
    private List<FileInfo> businessLicenseList;

    /**
     * 法定代表人身份证照片
     */
    @Transient
    private List<FileInfo> deputyIdCardPhotoList;
    /**
     * 企业形象图
     */
    @Transient
    private List<FileInfo> enterpriseImageList;

    /**
     * 企业用户集合
     */
    @Transient
    private List<User> userList;
}
