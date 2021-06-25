package com.github.wxiaoqi.security.jd.sys.feign.Service;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.jd.sys.feign.IFileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件服务类
 */
@Service
public class IFileInfoService {
    @Autowired
    private IFileInfo iFileInfo;

    /**
     * 通过表名和表id获得数据
     */
    public List<FileInfo> getByFormIdAndFormName(Integer formId, String formName) {
        return iFileInfo.getByFormIdAndFormName(formId, formName);
    }

    /**
     * 通过表名和表id集合获得数据
     */
    public List<FileInfo> getByFormIdsAndFormName(List<Integer> formIds, String formName) {
        //@RequestBody注解参数不能为null
        if (formIds == null) {
            return null;
        }
        return iFileInfo.getByFormIdsAndFormName(formIds, formName);
    }


    /**
     * 通过表id和表名删除数据,原url存入集合后返回
     */
    public List<FileInfo> commonDelByFIdAndFName(Integer formId, String formName, List<String> oldUrls) throws Exception {
        List<FileInfo> old = getByFormIdAndFormName(formId, formName);
        if (old != null && old.size() > 0) {
            addDelUrls(oldUrls, old);
            int index = iFileInfo.delByFormIdAndFormName(formId, formName);
            if (index != old.size()) {
                throw new Exception("批量删除文件失败");
            }
        }
        return old;
    }

    /**
     * 通过表id集合和表名删除数据,原url存入集合后返回
     */
    public List<FileInfo> commonDelByFIdsAndFName(List<Integer> formIds, String formName, List<String> oldUrls) throws Exception {
        List<FileInfo> old = getByFormIdsAndFormName(formIds, formName);
        if (old != null && old.size() > 0) {
            addDelUrls(oldUrls, old);
            int index = iFileInfo.delByFormIdsAndFormName(formIds, formName);
            if (index != old.size()) {
                throw new Exception("批量删除文件失败");
            }
        }
        return old;
    }


    /**
     * 上传单个文件返回url
     */
    public String uploadFile(MultipartFile file, String dir, String addFileName) {
        return iFileInfo.uploadFile(file, dir, addFileName);
    }

    /**
     * 批量添加数据和文件
     *
     * @throws Exception
     */
    public List<FileInfo> batchAddDataAndFile(Integer formId, String formName, Integer type,
                                              MultipartFile[] files, List<String> delUrls) throws Exception {
        if (files != null && files.length > 0) {
            List<FileInfo> fileInfos = new ArrayList<>();
            for (MultipartFile file : files) {
                String url = iFileInfo.uploadFile(file, formName, null);
                if (url != null) {
                    delUrls.add(url);
                    fileInfos.add(new FileInfo(formId, formName, type, url));
                }
            }
            int i = iFileInfo.insertByList(fileInfos);
            if (i != fileInfos.size()) {
                delFileByUrls(delUrls);
                throw new Exception(formName + "批量添加文件失败");
            }
            return fileInfos;
        }
        return null;
    }


    /**
     * 批量删除数据,原url存入集合后返回
     *
     * @throws Exception
     */
    public List<String> batchDelData(Integer[] idArr, List<String> oldUrls, List<String> delUrls) throws Exception {
        if (idArr != null) {
            List<Integer> ids = Arrays.asList(idArr);
            List<FileInfo> old = iFileInfo.selectByIds(ids);
            if (old != null && old.size() > 0) {
                addDelUrls(oldUrls, old);
                int index = iFileInfo.deleteByIds(ids);
                if (index != old.size()) {
                    delFileByUrls(delUrls);
                    throw new Exception("批量删除文件失败");
                }
            }
        }
        return oldUrls;
    }

    /**
     * 通过访问url集合删除文件
     */
    public void delFileByUrls(List<String> urls) {
        //@RequestBody注解参数不能为null
        if (urls == null) {
            return;
        }
        iFileInfo.delFileByUrls(urls);
    }

    /**
     * 存入文件url
     *
     * @param oldUrls
     * @param data
     */
    private void addDelUrls(List<String> oldUrls, List<FileInfo> data) {
        List<String> urls = data.stream().map(FileInfo::getUrl).collect(Collectors.toList());
        oldUrls.addAll(urls);
    }

}
