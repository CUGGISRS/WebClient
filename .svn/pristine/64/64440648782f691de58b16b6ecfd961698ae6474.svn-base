package com.github.wxiaoqi.security.dzsw.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author TsaiJun
 * 图片上传文件配置
 */
@Component
public class FileUploadConfig {

    /**
     * 图片文件保存目录
     */
    @Value("${uploadconfig.IMGSavePath}")
    private String imgSavePath;

    public String getImgSavePath() {
        return imgSavePath;
    }

    public void setImgSavePath(String imgSavePath) {
        this.imgSavePath = imgSavePath;
    }

    /**
     * 视频文件保存目录
     */
    @Value("${uploadconfig.videoSavePath}")
    private String videoSavePath;

    public String getVideoSavePath() {
        return videoSavePath;
    }

    public void setVideoSavePath(String videoSavePath) {
        this.videoSavePath = videoSavePath;
    }

    /**
     * 缩略图保存目录
     */
    @Value("${uploadconfig.thumbnailSavePath}")
    private String thumbnailSavePath;

    public String getThumbnailSavePath() {
        return thumbnailSavePath;
    }

    public void setThumbnailSavePath(String thumbnailSavePath) {
        this.thumbnailSavePath = thumbnailSavePath;
    }

}
