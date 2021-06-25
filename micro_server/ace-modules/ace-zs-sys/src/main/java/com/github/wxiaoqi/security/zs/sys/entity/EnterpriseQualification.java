package com.github.wxiaoqi.security.zs.sys.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业资质
 */
@Table(name = "aqzs_enterprise_qualification")
@Data
@NoArgsConstructor
public class EnterpriseQualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 企业资质代码
     */
    @Column(name = "aptitude_code")
    private String aptitudeCode;

    /**
     * 企业资质类型
     */
    @Column(name = "aptitude_type")
    private String aptitudeType;

    /**
     * 发证单位
     */
    @Column(name = "certification_unit_name")
    private String certificationUnitName;

    /**
     * 发证时间
     */
    @Column(name = "certification_time")
    private Date certificationTime;

    /**
     * 有效时间
     */
    @Column(name = "effective_time")
    private Date effectiveTime;

    /**
     * 许可范围
     */
    @Column(name = "permit_range")
    private String permitRange;

    /**
     * 企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 企业名称
     */
    @Column(name = "enterprise_name")
    private String enterpriseName;

    /**
     * 资质证书
     */
    @Transient
    private List<FileInfo> aptitudeCertificateList;

    public EnterpriseQualification(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
