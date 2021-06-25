package com.github.wxiaoqi.security.dzsw.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "com_video_info")
public class VideoInfo {
    /**
     * 视频ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 视频名称
     */
    @Column(name = "video_name")
    private String videoName;

    /**
     * 路径
     */
    private String path;

    /**
     * 发布时间
     */
    @Column(name = "pub_time")
    private Date pubTime;

    /**
     * 作者
     */
    private String author;

    /**
     * 浏览量
     */
    @Column(name = "watch_times")
    private String watchTimes;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频分类
     */
    private String type;

    /**
     * 0表示未完成，1 表示完成
     */
    private Integer state;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 获取视频ID
     *
     * @return id - 视频ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置视频ID
     *
     * @param id 视频ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取视频名称
     *
     * @return video_name - 视频名称
     */
    public String getVideoName() {
        return videoName;
    }

    /**
     * 设置视频名称
     *
     * @param videoName 视频名称
     */
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    /**
     * 获取路径
     *
     * @return path - 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路径
     *
     * @param path 路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取发布时间
     *
     * @return pub_time - 发布时间
     */
    public Date getPubTime() {
        return pubTime;
    }

    /**
     * 设置发布时间
     *
     * @param pubTime 发布时间
     */
    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取浏览量
     *
     * @return watch_times - 浏览量
     */
    public String getWatchTimes() {
        return watchTimes;
    }

    /**
     * 设置浏览量
     *
     * @param watchTimes 浏览量
     */
    public void setWatchTimes(String watchTimes) {
        this.watchTimes = watchTimes;
    }

    /**
     * 获取视频描述
     *
     * @return description - 视频描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置视频描述
     *
     * @param description 视频描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取视频分类
     *
     * @return type - 视频分类
     */
    public String getType() {
        return type;
    }

    /**
     * 设置视频分类
     *
     * @param type 视频分类
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取0表示未完成，1 表示完成
     *
     * @return state - 0表示未完成，1 表示完成
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置0表示未完成，1 表示完成
     *
     * @param state 0表示未完成，1 表示完成
     */
    public void setState(Integer state) {
        this.state = state;
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

    /**
     * 扩展名
     *
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

    @Transient
    /**
     * 缩略图
     */
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
