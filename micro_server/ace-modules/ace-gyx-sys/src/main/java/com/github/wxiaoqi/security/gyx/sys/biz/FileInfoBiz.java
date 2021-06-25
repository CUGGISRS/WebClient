package com.github.wxiaoqi.security.gyx.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.gyx.sys.config.FileUploadConfig;
import com.github.wxiaoqi.security.gyx.sys.entity.FileInfo;
import com.github.wxiaoqi.security.gyx.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.gyx.sys.util.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileInfoBiz extends BaseBiz<FileInfoMapper, FileInfo> {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String ip;

    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    public boolean addOneFile(MultipartFile[] files, String tableName, int tableId) {
        try {
            // 获取当前年月
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            // path为图片存放地址
            String path = ROOT_PATH + SON_PATH + YMPath;
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
                //数据库保存的格式为： 2020-09/791eae82b4bb4d69818b6d1542ec055d.jpg
                fileInfo.setPath(YMPath + fileName);
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

    // 获取文件url列表
    public List<String> getFileUrlList(String tableName, int tableId) {
        List<String> result = new ArrayList<>();
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("linkTable", tableName);
        criteria.andEqualTo("tableId", tableId);
        List<FileInfo> list = selectByExample(example);
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        for (FileInfo fileInfo : list) {
            String s = ip + ":" + port;
            result.add(s + fileInfo.getPath());
        }
        return result;
    }

}
