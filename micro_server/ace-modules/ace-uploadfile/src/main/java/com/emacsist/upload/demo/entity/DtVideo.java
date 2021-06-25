package com.emacsist.upload.demo.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dt_uploadvideo")
public class DtVideo {
    /**
     * 编号(cj创建)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * 缩略图
     */
    @Transient
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 扩展名
     * @return
     */
    @Transient
    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * 视频名称
     */
    @Column(name = "Videoname")
    private String videoname;

    /**
     * 分类
     */
    @Column(name = "Classification")
    private String classification;

    /**
     * 视频上传时间
     */
    @Column(name = "UploadDate")
    private Date uploaddate;

    /**
     * 上传人
     */
    @Column(name = "UserId")
    private Integer userid;

    /**
     * 备注
     */
    @Column(name = "Remark")
    private String remark;

    /**
     * 存储路径 
     */
    @Column(name = "Url")
    private String url;

    /**
     * 文件上传状态0表示未上传成功，1表示上传成功
     */
    @Column(name = "State")
    private Integer state;

    /**
     * 获取编号(cj创建)
     *
     * @return id - 编号(cj创建)
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置编号(cj创建)
     *
     * @param id 编号(cj创建)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取视频名称
     *
     * @return videoname - 视频名称
     */
    public String getVideoname() {
        return videoname;
    }

    /**
     * 设置视频名称
     *
     * @param videoname 视频名称
     */
    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    /**
     * 获取分类
     *
     * @return classification - 分类
     */
    public String getClassification() {
        return classification;
    }

    /**
     * 设置分类
     *
     * @param classification 分类
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * 获取视频上传时间
     *
     * @return uploaddate - 视频上传时间
     */
    public Date getUploaddate() {
        return uploaddate;
    }

    /**
     * 设置视频上传时间
     *
     * @param uploaddate 视频上传时间
     */
    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    /**
     * 获取上传人
     *
     * @return userid - 上传人
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置上传人
     *
     * @param userid 上传人
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取存储路径 
     *
     * @return url - 存储路径 
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置存储路径 
     *
     * @param url 存储路径 
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取文件上传状态0表示未上传成功，1表示上传成功
     *
     * @return state - 文件上传状态0表示未上传成功，1表示上传成功
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置文件上传状态0表示未上传成功，1表示上传成功
     *
     * @param state 文件上传状态0表示未上传成功，1表示上传成功
     */
    public void setState(Integer state) {
        this.state = state;
    }
}