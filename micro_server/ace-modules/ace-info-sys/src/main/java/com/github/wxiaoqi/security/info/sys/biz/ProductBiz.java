package com.github.wxiaoqi.security.info.sys.biz;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.entity.AuthInfo;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAgriculturalInfo;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAndFile;
import com.github.wxiaoqi.security.info.sys.mapper.AuthInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.ProductsAgriculturalInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.info.sys.utils.FileUtils;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description: 特色农产品
 * @author: gsy
 * @create: 2020-09-14 09:39
 **/

@RestController
@RequestMapping("product")
@Transactional(rollbackFor = Exception.class)
public class ProductBiz extends BaseBiz<ProductsAgriculturalInfoMapper, ProductsAgriculturalInfo> {

    @Resource
    private ProductsAgriculturalInfoMapper productsMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private AuthInfoMapper authInfoMapper;

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

    public ObjectRestResponse<T> addProduct(Map<String, String> map, MultipartFile[] files) {
        //基本信息
        ProductsAgriculturalInfo Products = new ProductsAgriculturalInfo();
        Products.setName(map.get("name"));
        Products.setAllType(map.get("allType"));
        Products.setType(map.get("type"));
        Products.setDescription(map.get("description"));
        //关联信息
        if (map.containsKey("companyId")) {
            Products.setCompanyId(Integer.parseInt(map.get("companyId")));
        }
        if (map.containsKey("authId")) {
            Products.setAuthId(Integer.parseInt(map.get("authId")));
        }

        int i = productsMapper.insertSelective(Products);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //农产品id
        Integer productId = Products.getId();

//        //三品一标认证
//        AuthInfo authInfo = new AuthInfo();
//        authInfo.setAuthName(map.get("authName"));
//        authInfo.setNumber(map.get("number"));
//        authInfo.setInstitution(map.get("institution"));
//        authInfo.setAuthDate(map.get("authDate"));
//        authInfo.setPastDate(map.get("pastDate"));
//        authInfo.setProductsId(Products.getId());
//        //三品一标数据库插入
//        int insertSelective = authInfoMapper.insertSelective(authInfo);

        //获取本机IP
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
////            LOG.error("get server host Exception e:", e);
//        }
        //获取图片文件
        try {
            //根路径 + 子目录 "F:" + "/infoSys/"
            String path = ROOT_PATH + SON_PATH;
            //判断是否有路径
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }

            for (MultipartFile file : files) {
                // 将图片文件保存到服务器，同时返回上传后图片的名字
                String imageName = UploadFile.uploadFile(file, path);
                FileInfo productBaseImg = new FileInfo();
                //农产品id
                productBaseImg.setTableId(productId);
                productBaseImg.setLinkTable("zsxx_products_agricultural_info");
                //图片路径 主机ip + 端口 + (根路径 + 子目录)已映射 +文件名
                //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
                //图片路径存放数据库
                productBaseImg.setPath(SON_PATH + imageName);

                int insert = fileInfoMapper.insertSelective(productBaseImg);
                System.out.println("上传成功");
                if (insert == 0) {
                    return new ObjectRestResponse<>(StatusCode.FAIL, true);
                }
            }

            //三品一标图片
//            int insert = saveMultipartFile(AuthInfoFiles, path, "zsxx_auth_info", authInfo.getId());
//            System.out.println("三品一标图片上传成功!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ObjectRestResponse(StatusCode.SUCCESS, productId);
    }

    public ObjectRestResponse<T> delProduct(Integer[] ids) {
        int i = 0;
        try {
            Example example = new Example(FileInfo.class);
            Example.Criteria criteria = example.createCriteria();
            List<Integer> delIdList = new ArrayList<>();
            Collections.addAll(delIdList, ids);
            criteria.andIn("tableId", delIdList);
            criteria.andEqualTo("linkTable", "zsxx_products_agricultural_info");
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
            //根据id删除中间表数据库保存的信息
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //根据List<FileInfo>，取出路径进行批量删除图片
            new FileUtils().delFilesByPath(fileInfos);
            //主表删除
            for (Integer id : ids) {
                i = productsMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception e) {
            return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
        }
        if (i > 0) {
            return new ObjectRestResponse<T>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<T>(StatusCode.FAIL, false);
    }

    public ObjectRestResponse<JSONObject> all(int[] ints, String authType, String allType) {
        //获取本机ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        //查询农产品list
        List<ProductsAndFile> ProductsList = productsMapper.listProduct(ints[0], ints[1], authType, allType);
        for (ProductsAndFile product : ProductsList) {
            //加上服務器ip和端口
            List<FileInfo> imgLists = product.getImgLists();
            for (FileInfo img : imgLists) {
                img.setPath(s + img.getPath());
            }
        }
        //查询总数
        int i = mapper.getLinkCount(authType, allType);
        return new ObjectRestResponse<JSONObject>(StatusCode.SUCCESS, CommonUtil.successPage(ProductsList, i), true);
    }

    public ObjectRestResponse<T> updateProduct(ProductsAgriculturalInfo products, Integer[] delIds, MultipartFile[] files) {
        //1.修改农产品信息
        int i = productsMapper.updateByPrimaryKeySelective(products);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FILEDELETEFAIL, false);
        }
        //2.删除旧图片文件&删除就图片数据库
        if (delIds != null) {
            try {
                //通过id查询图片信息
                // 删除数量
                int i1 = delImgByIds(delIds);
            } catch (Exception e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
            }
        }
        //新增新图片
        //根路径 + 子目录 "F:" + "/infoSys/"
        String path = ROOT_PATH + SON_PATH;
        //判断是否有路径
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        //3.上传新图片文件到服务器 & 将农产品信息与图片关联到数据库
        int insert = saveMultipartFile(files, path, "zsxx_products_agricultural_info", products.getId());
        //存在文件且上传数量为0，返回异常
        if (files.length > 0 && insert == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
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
            FileInfo fileInfo = new FileInfo();
            //农产品id
            fileInfo.setTableId(primaryKey);
            fileInfo.setLinkTable(linkTable);
            //图片路径 主机ip + 端口 + (根路径 + 子目录)已映射 +文件名
            //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
            //图片路径存放数据库
            fileInfo.setPath(SON_PATH + imageName);
            insert += fileInfoMapper.insertSelective(fileInfo);
            System.out.println("上传成功");
        }
        return insert;
    }

    //传入 待删除图片数组ids，删除图片
    public int delImgByIds(Integer[] delIds) throws Exception {
        //int类型数组转为List，提供tk.mybatis in查询
        List<Integer> delIdList = new ArrayList<>();
        Collections.addAll(delIdList, delIds);
        //根据id查出相应的图片路径
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", delIdList);
        criteria.andEqualTo("linkTable", "zsxx_products_agricultural_info");
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
        //根据id删除中间表数据库保存的信息
        int delete = batchDeleteById(delIdList);
        //根据List<FileInfo>，取出路径进行批量删除图片
        int i = new FileUtils().delFilesByPath(fileInfos);
        return delete;
    }

    //批量删除文件信息中间表
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

    public ObjectRestResponse<ProductsAndFile> queryById(Integer id) {
        //查询单条农产品信息
        ProductsAgriculturalInfo product = selectById(id);
        if (product == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //查询图片信息
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("linkTable", "zsxx_products_agricultural_info");
        criteria.andEqualTo("tableId", id);
        //图片list
        List<FileInfo> ImgLists = fileInfoMapper.selectByExample(example);
        //为图片添加ip和端口
        addHostAndPort(ImgLists);
        //bean复制
        ProductsAndFile productsAndFile = new ProductsAndFile();
        BeanUtil.copyProperties(product, productsAndFile);
        productsAndFile.setImgLists(ImgLists);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, productsAndFile);
    }

    /**
     * @Description: href加上本机ip和域名
     * @Author: gsy
     * @Date: 2020/9/15
     */
    public void addHostAndPort(List<FileInfo> imgList) {
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        for (FileInfo img : imgList) {
            img.setPath(s + img.getPath());
        }
//        return imgList;
    }
}
