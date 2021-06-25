package com.github.wxiaoqi.security.info.sys.utils;

import cn.hutool.core.io.FileUtil;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;

import java.io.File;
import java.util.List;

/**
 * @description: 文件操作
 * @author: gsy
 * @create: 2020-09-16 20:03
 **/

public class FileUtils {

    //根据List<实体>，取出路径删除图片
    public int delFilesByPath(List<FileInfo> fileInfos) {
        int i = 0;
        for (FileInfo fileinfo : fileInfos) {
            File file = new File(fileinfo.getPath());
            // 为文件时调用删除文件方法
            if (FileUtil.isFile(file)) {
                if (FileUtil.del(file)) {
                    i++;
                    System.out.println("删除成功");
                }
            }
        }
        return i;
    }

    public boolean delFilesByPath(String path) {
        File file = new File(path);
        // 为文件时调用删除文件方法
        if (FileUtil.isFile(file)) {
            if (FileUtil.del(file)) {
                System.out.println("删除成功");
                return true;
            }
        }
        return false;
    }
}
