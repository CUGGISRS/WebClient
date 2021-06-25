package com.github.wxiaoqi.security.dzsw.sys.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UploadFile {
    public static String uploadFile(MultipartFile file, String path) throws IOException {
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
