package com.github.wxiaoqi.security.com.sys.biz;

import com.github.wxiaoqi.security.com.sys.config.MyWebMvcConfigurer;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.FileUtils;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件
 */
@Service
public class FileInfoBiz extends BaseBiz<FileInfoMapper, FileInfo> {

    @Autowired
    private MyWebMvcConfigurer myWebMvcConfigurer;

    /**
     * 通过表名和表id获得数据
     */
    public List<FileInfo> getByFormIdAndFormName(Integer formId, String formName) {
        if (MyObjectUtil.noNullOrEmpty(formId) && MyObjectUtil.noNullOrEmpty(formName)) {
            Map<String, Object> map = new HashMap<>();
            map.put("formId", formId);
            map.put("formName", formName);
            return getByToMap(map);
        }
        return null;
    }

    /**
     * 通过表名和表id删除数据
     */
    public int delByFormIdAndFormName(Integer formId, String formName) {
        if (MyObjectUtil.noNullOrEmpty(formId) && MyObjectUtil.noNullOrEmpty(formName)) {
            Map<String, Object> map = new HashMap<>();
            map.put("formId", formId);
            map.put("formName", formName);
            return delByToMap(map);
        }
        return 0;
    }

    /**
     * 通过表名和表id集合获得数据
     */
    public List<FileInfo> getByFormIdsAndFormName(List<Integer> formIds, String formName) {
        String propertyName = "formId";
        if (MyObjectUtil.noNullOrEmpty(formName)) {
            Map<String, Object> map = new HashMap<>();
            map.put("formName", formName);
            return getByInFiledMayToMap(formIds, propertyName, map);
        }
        return null;
    }

    /**
     * 通过表名和表id集合批量删除数据
     */
    public int delByFormIdsAndFormName(List<Integer> formIds, String formName) {
        String propertyName = "formId";
        if (MyObjectUtil.noNullOrEmpty(formName)) {
            Map<String, Object> map = new HashMap<>();
            map.put("formName", formName);
            return delByInFiledMayToMap(formIds, propertyName, map);
        }
        return 0;
    }


    /**
     * 上传文件到保存路径的某个文件夹下，返回访问url(生成文件名前加内容)
     */
    public String uploadFile(MultipartFile file, String dir, String addFileName) throws IOException {
        String path = myWebMvcConfigurer.getSavePath() + dir;
        String fileName = FileUtils.uploadFile(file, path, addFileName);
        if (fileName != null) {
            String url = myWebMvcConfigurer.getVisitPrefix() + dir + "/" + fileName;
            return url;
        }
        return null;
    }

    /**
     * 通过访问url删除文件
     */
    public void delFileByUrl(String url) {
        String visitSuffix = myWebMvcConfigurer.getVisitPrefix();
        int len;
        if (url != null && (len = url.indexOf(visitSuffix)) != -1) {
            url = myWebMvcConfigurer.getSavePath() + url.substring(len + visitSuffix.length());
            FileUtils.deleteFile(url);
        }
    }

    /**
     * 通过访问url集合删除文件
     */
    public void delFileByUrls(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            for (String url : urls) {
                delFileByUrl(url);
            }
        }

    }


}
