package com.github.wxiaoqi.security.info.sys.biz;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.entity.AuthInfo;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import com.github.wxiaoqi.security.info.sys.mapper.AuthInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 三品一标认证
 * @author: gsy
 * @create: 2020-09-14 17:26
 **/

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthBiz {

    @Resource
    AuthInfoMapper authInfoMapper;

    @Resource
    FileInfoMapper fileInfoMapper;

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

    public ObjectRestResponse<T> addAuthProduct(AuthInfo authInfo, MultipartFile[] files) {
        //新增三品一标认证
        int insert = authInfoMapper.insertSelective(authInfo);
        if (insert == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        Integer authId = authInfo.getId();
        String path = ROOT_PATH + SON_PATH;

        if (files != null) {
            // 将图片文件保存到服务器，同时返回上传后图片的名字
            String imageName = null;
            FileInfo authImg = new FileInfo();
            try {
                int chang = 0;
                //获取本机ip
//                    String host = InetAddress.getLocalHost().getHostAddress();
                for (MultipartFile file : files) {
                    imageName = UploadFile.uploadFile(file, path);
                    //三品一标id
                    authImg.setTableId(authId);
                    authImg.setLinkTable("zsxx_auth_info");
                    //图片路径 主机ip + 端口 + (根路径 + 子目录)已映射 +文件名
                    authImg.setPath(ip + ":" + port + SON_PATH + imageName);
                    chang = fileInfoMapper.insertSelective(authImg);
                    System.out.println("上传成功");
                    if (chang == 0) {
                        return new ObjectRestResponse<>(StatusCode.FAIL, false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> del(ArrayList<Integer> id) {
        Example example = new Example(AuthInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", id);
        int i = authInfoMapper.deleteByExample(example);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> update(AuthInfo authInfo, Integer[] delIds, MultipartFile[] files) {
        //修改数据库
        int i = authInfoMapper.updateByPrimaryKeySelective(authInfo);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //2.删除旧图片文件&删除就图片数据库
        if (delIds != null) {
            try {
                delImgByIds(delIds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3.上传新图片文件到服务器 & 将农产品信息与图片关联到数据库
        if (files != null) {
            String path = ROOT_PATH + SON_PATH;
            int insert = saveMultipartFile(files, path, "zsxx_auth_info", authInfo.getId());
            //存在文件且上传数量为0，返回异常
            if (files.length != insert) {
                return new ObjectRestResponse<>(StatusCode.FAIL, false);
            }
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    /**
     * @Description: 保存多图片至服务器
     * @Param: 图片数组，路径，关联表名，关联表行id
     * @return: 上传的数量
     * @Author: gsy
     * @Date: 2020/9/15
     */
    //"zsxx_products_agricultural_info"
    public int saveMultipartFile(MultipartFile[] files, String path, String linkTable, Integer primaryKey) {
        String imageName = null;
        int insert = 0;
        for (MultipartFile file : files) {
            // 将图片文件保存到服务器，同时返回上传后图片的名字
            try {
                imageName = UploadFile.uploadFile(file, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileInfo productBaseImg = new FileInfo();
            //农产品id
            productBaseImg.setTableId(primaryKey);
            productBaseImg.setLinkTable(linkTable);
            //图片路径 主机ip + 端口 + (根路径 + 子目录)已映射 +文件名
            //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
            //图片路径存放数据库
            productBaseImg.setPath(SON_PATH + imageName);
            insert += fileInfoMapper.insertSelective(productBaseImg);
            System.out.println("上传成功");
        }
        return insert;
    }

    //根据 待删除图片数组ids，删除图片
    public int delImgByIds(Integer[] delIds) throws Exception {
        //int类型数组转为List，提供tk.mybatis in查询
        List<Integer> delIdList = new ArrayList<>();
        Collections.addAll(delIdList, delIds);
        //根据id查出相应的图片路径
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", delIdList);
        criteria.andEqualTo("linkTable", "zsxx_auth_info");
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
        //根据id删除数据库保存的信息
        batchDeleteById(delIdList);
        //根据路径删除
        int i = 0;
        for (FileInfo fileinfo : fileInfos) {
            String path = fileinfo.getPath();
            File file = new File(path);
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

    //批量删除
    public int batchDeleteById(List<Integer> ids) throws Exception {
        if (ids == null) {
            return 0;
        }
        for (Integer id : ids) {
            int index = fileInfoMapper.deleteByPrimaryKey(id);
            if (index == 0) {
                throw new Exception("id为" + id + "的数据删除失败");
            }
        }
        return ids.size();
    }

    public ObjectRestResponse<T> query(Integer page, Integer size) {
        authInfoMapper.query(page, size);
        return null;
    }

    public ObjectRestResponse<List<JSONObject>> getByProductId(Integer productId) {
        List<JSONObject> byProductId = authInfoMapper.getByProductId();
        return new ObjectRestResponse<>(StatusCode.SUCCESS, byProductId);
    }

    public ObjectRestResponse getByAuthId(Integer authId) {
        try {
            List<JSONObject> authInfoList = authInfoMapper.getAuthInfoById(authId);

            for (JSONObject jsonObject : authInfoList) {
                ArrayList<JSONObject> imgLists = (ArrayList<JSONObject>) jsonObject.get("fileLists");
                for (int i = 0; i < imgLists.size(); i++) {
                    String outPath = ip + ":" + port + imgLists.get(i).get("path");
                    imgLists.get(i).put("path", outPath);
                }
            }
            return new ObjectRestResponse<>(StatusCode.SUCCESS, authInfoList.get(0));
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }
}
