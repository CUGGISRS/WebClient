package com.github.wxiaoqi.security.info.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.info.sys.entity.CarouselInfo;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import com.github.wxiaoqi.security.info.sys.mapper.CarouselInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.FileUtils;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: gsy
 * @create: 2020-09-16 18:00
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CarouselBiz {

    @Resource
    CarouselInfoMapper carouselInfoMapper;
    @Resource
    FileInfoMapper fileInfoMapper;
    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    String ROOT_PATH;
    //子目录
    @Value("${UploadFile.SON_PATH}")
    String SON_PATH;

    @Value("${server.port}")
    public String port;

    @Value("${server.ip}")
    public String ip;

    //图片保存路径
    private final String path = ROOT_PATH + SON_PATH;

    public ObjectRestResponse<T> add(CarouselInfo carouselInfo, MultipartFile file) {
        //保存图片服务器 & 保存信息到数据库 获取中间表id
        int fileId = saveMultipartFile(file, "com_carousel_info", carouselInfo.getId());
        //将中间表id存入轮播图信息
        carouselInfo.setImg(fileId);
        //保存轮播图信息
        int insert = carouselInfoMapper.insertSelective(carouselInfo);
        if (insert == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> update(CarouselInfo carouselInfo, MultipartFile file) {
        int i = carouselInfoMapper.updateByPrimaryKey(carouselInfo);
        //如果传入新图片，则删除旧图片
        if (file != null) {
            try {
                //通过imgId,寻找src
                Example example = new Example(FileInfo.class);
                Example.Criteria criteria = example.createCriteria();
                //文件表id
                criteria.andEqualTo("id", carouselInfo.getImg());
                //查询文件信息
                FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(carouselInfo.getImg());
                //根据文件信息，磁盘删除
                boolean b = new FileUtils().delFilesByPath(ROOT_PATH + fileInfo.getPath());
                //删除成功，插入新图片
//                if (b){
                //获取新的文件路径
                String filePath = UploadFile.uploadFile(file, ROOT_PATH + SON_PATH);
                //为文件中间表 更新 新的文件路径
                fileInfo.setPath(SON_PATH + filePath);
                int change = fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
                if (change == 0) {
                    return new ObjectRestResponse<>(StatusCode.FAIL, "更新新文件失败!");
                }
//                }else {
//                    return new ObjectRestResponse<>(StatusCode.FAIL,"磁盘文件不存在，数据库存在，操作失败！");
//                }
            } catch (Exception e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
            }
        }
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    /**
     * @Description: 保存图片至服务器
     * @Param: 图片，路径，关联表名，关联表行id
     * @return: 上传的数量
     * @Author: gsy
     * @Date: 2020/9/15
     */
    //"zsxx_products_agricultural_info"
    public int saveMultipartFile(MultipartFile file, String linkTable, Integer primaryKey) {
        String imageName = null;
        try {
            // 将图片文件保存到服务器，同时返回上传后图片的名字
            imageName = UploadFile.uploadFile(file, ROOT_PATH + SON_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInfo fileInfo = new FileInfo();
        //农产品id
        fileInfo.setTableId(primaryKey);
        fileInfo.setLinkTable(linkTable);
        //图片路径 主机ip + 端口 + (根路径 + 子目录)已映射 +文件名
        //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
        //图片路径存放数据库
        if (imageName != null) {
            fileInfo.setPath(SON_PATH + imageName);
            System.out.println("上传成功");
        }
        int fileId = fileInfoMapper.insertSelective(fileInfo);
        return fileInfo.getId();
    }

    public ObjectRestResponse<Map<String, Object>> page(Integer page, Integer size, String targetSystem) {
        List<JSONObject> objectList = carouselInfoMapper.page(page, size, targetSystem);
       /* String localhostStr = NetUtil.getLocalhostStr();
        if (null == localhostStr){
            return new ObjectRestResponse<>(StatusCode.FAIL,"获取服务器ip失败");
        }*/
        for (JSONObject object : objectList) {
            object.put("path", "http://" + ip + ":" + port + object.get("path"));
        }
        Example example = new Example(CarouselInfo.class);
        example.createCriteria().andEqualTo("targetSystem", targetSystem);
        int i = carouselInfoMapper.selectCountByExample(example);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", objectList);
        map.put("total", i);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, map, true);
    }

    public ObjectRestResponse<T> del(Integer id) {
        //根据路径删除图片
        CarouselInfo carouselInfo = carouselInfoMapper.selectByPrimaryKey(id);
        FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(carouselInfo.getImg());
        if (fileInfo != null) {
            boolean b = new FileUtils().delFilesByPath(ROOT_PATH + fileInfo.getPath());
        }
//        if (!b) {
//            return new ObjectRestResponse<>(StatusCode.FAIL, false);
//        }
        //数据库物理删除
        int i = carouselInfoMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    // 更新轮播图发布状态
    public ObjectRestResponse updateStatus(int id, String status) {
        try {
            CarouselInfo carouselInfo = carouselInfoMapper.selectByPrimaryKey(id);
            carouselInfo.setStatus(status);
            int affect = carouselInfoMapper.updateByPrimaryKeySelective(carouselInfo);
            if (affect == 0) {
                return new ObjectRestResponse<>(StatusCode.FAIL, false);
            }
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
    }
}
