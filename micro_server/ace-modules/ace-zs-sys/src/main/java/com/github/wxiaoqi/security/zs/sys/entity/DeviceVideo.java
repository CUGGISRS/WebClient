package com.github.wxiaoqi.security.zs.sys.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * 物联网设备视频
 */
@Table(name = "xph.xph_device_video")
@Data
public class DeviceVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属企业id
     */
    @Column(name = "enterprise_id")
    private Integer enterpriseId;

    /**
     * 设备序列号
     */
    @Column(name = "serial_num")
    private String serialNum;

    /**
     * 通道号
     */
    @Column(name = "pass_num")
    private String passNum;

    /**
     * 权限token
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * 验证码
     */
    @Column(name = "verify_code")
    private String verifyCode;

    /**
     * 是否自动播放(1是,0否)
     */
    @Column(name = "is_auto_play")
    private Integer isAutoPlay;

    /**
     * 是否开启音频(1是,0否)
     */
    @Column(name = "is_open_audio")
    private Integer isOpenAudio;

    /**
     * 标题
     */
    private String title;
}
