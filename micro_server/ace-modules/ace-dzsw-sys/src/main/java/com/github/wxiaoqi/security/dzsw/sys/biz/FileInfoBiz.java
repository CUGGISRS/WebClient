package com.github.wxiaoqi.security.dzsw.sys.biz;

import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.net.NetUtil;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.dzsw.sys.config.FileUploadConfig;
import com.github.wxiaoqi.security.dzsw.sys.entity.FileInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.service.ThumbnailService;
import com.github.wxiaoqi.security.dzsw.sys.utils.StringTools;
import com.github.wxiaoqi.security.dzsw.sys.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileInfoBiz extends BaseBiz<FileInfoMapper, FileInfo> {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private ThumbnailService thumbnailService;

    @Value("${server.port}")
    public String port;

    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    // 视频子目录
    @Value("${UploadFile.VIDEO_PATH}")
    private String VIDEO_PATH;

    // 缩略图子目录
    @Value("${UploadFile.THUMB_PATH}")
    private String THUMB_PATH;

    /**
     * 通过关联表id和表名查询数据
     */
    public List<FileInfo> getByTableIdAndLinkTable(Integer tableId,String linkTable){
        if(tableId==null|| StringHelper.isNullOrEmpty(linkTable)){
            return null;
        }
        Map<String,Object> toMap=new HashMap<>();
        toMap.put("tableId",tableId);
        toMap.put("linkTable",linkTable);
        return getByToMap(toMap);
    }

    public boolean addOneFile(MultipartFile[] files, String tableName, int tableId) {
        try {
//            String path = fileUploadConfig.getImgSavePath();
            // 获取当前年月
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            String path = ROOT_PATH + SON_PATH + YMPath;
//            path = path + YMPath;
            //判断是否有路径
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            for (MultipartFile file : files) {
                // 将图片文件保存到服务器，同时返回上传后图片的名字
                String fileName = UploadFile.uploadFile(file, path);
                FileInfo fileInfo = new FileInfo();
                // 关联表id
                fileInfo.setTableId(tableId);
                // 关联表名
                fileInfo.setLinkTable(tableName);
                fileInfo.setPath(SON_PATH + YMPath + fileName);
                int insert = insertSelective(fileInfo);
                System.out.println("上传成功");
                if (insert == 0) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String addVideo(MultipartFile file, String tableName, int tableId) {
        try {
//            String path = fileUploadConfig.getImgSavePath();
            // 获取当前年月
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            String videoPath = ROOT_PATH + VIDEO_PATH + YMPath;
            String thumbPath = ROOT_PATH + THUMB_PATH + YMPath;
//            path = path + YMPath;
            //判断是否有路径
            if (!new File(videoPath).exists()) {
                new File(videoPath).mkdirs();
            }
            if (!new File(thumbPath).exists()) {
                new File(thumbPath).mkdirs();
            }
            // 将图片文件保存到服务器，同时返回上传后图片的名字
            String fileName = UploadFile.uploadFile(file, videoPath);
            FileInfo fileInfo = new FileInfo();
            // 关联表id
            fileInfo.setTableId(tableId);
            // 关联表名
            fileInfo.setLinkTable(tableName);
            fileInfo.setPath(VIDEO_PATH + YMPath + fileName);
            int insert = insertSelective(fileInfo);
            System.out.println("视频上传成功");
            if (insert == 0) {
                return null;
            }
            // 生成缩略图
            thumbnailService.generateThumb(fileName, videoPath, thumbPath);
            return VIDEO_PATH + YMPath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取url列表
     *
     * @param tableName
     * @param id
     * @return
     */
    public List<String> urlListByTableAndId(String tableName, int id) {
        List<String> result = new ArrayList<>();
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("linkTable", tableName);
        criteria.andEqualTo("tableId", id);
        String localhostStr = NetUtil.getLocalhostStr();
        List<FileInfo> fileInfoList = selectByExample(example);
        for (FileInfo fileInfo : fileInfoList) {
            String url = fileInfo.getPath();
            if (url != null && localhostStr != null) {
                url = localhostStr + ":" + port + url;
                result.add(url);
            }
        }
        return result;
    }

}
