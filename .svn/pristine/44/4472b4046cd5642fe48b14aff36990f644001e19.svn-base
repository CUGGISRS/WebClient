package com.github.wxiaoqi.security.info.sys.utils;

import cn.hutool.core.util.IdUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @description: 上传图片
 * @author: gsy
 * @create: 2020-09-14 11:39
 **/

public class UploadFile {

    public static String uploadFile(MultipartFile file, String path) throws IOException {
        //判断是否有路径
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        String name = file.getOriginalFilename();
        String suffixName = name.substring(name.lastIndexOf("."));
        // 生成图片id
        String imgid = IdUtil.simpleUUID();
        String fileName = imgid + suffixName;
        File tempFile = new File(path, fileName);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();
        file.transferTo(tempFile);
        return tempFile.getName();
    }
}
