package com.emacsist.upload.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author TsaiJun
 * 视频文件和缩略图文件保存目录
 */
@Component
public class FileUploadConfig {

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
