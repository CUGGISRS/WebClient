package com.github.wxiaoqi.security.gyx.sys.config;

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
    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    public String getImgSavePath() {
        return ROOT_PATH + SON_PATH;
    }


}
