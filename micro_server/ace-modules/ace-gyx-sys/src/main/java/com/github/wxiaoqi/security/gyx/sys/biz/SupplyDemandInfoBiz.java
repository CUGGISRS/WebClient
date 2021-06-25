package com.github.wxiaoqi.security.gyx.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.gyx.sys.entity.FileInfo;
import com.github.wxiaoqi.security.gyx.sys.entity.SupplyDemandAndFile;
import com.github.wxiaoqi.security.gyx.sys.entity.SupplyDemandInfo;
import com.github.wxiaoqi.security.gyx.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.gyx.sys.mapper.SupplyDemandInfoMapper;
import com.github.wxiaoqi.security.gyx.sys.util.FileUtils;
import com.github.wxiaoqi.security.gyx.sys.util.UploadFile;
import com.github.wxiaoqi.security.info.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplyDemandInfoBiz extends BaseBiz<SupplyDemandInfoMapper, SupplyDemandInfo> {
    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private SupplyDemandInfoMapper supplyDemandInfoMapper;

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

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public TableResultResponse<SupplyDemandInfo> selectSimpleByQuery(Query query, String type) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<SupplyDemandInfo> list = null;
        if (type == null || type.isEmpty()) {
            list = mapper.selectSimple();
        } else {
            list = mapper.selectSimpleByType(type);
        }
        return new TableResultResponse<SupplyDemandInfo>(result.getTotal(), list);
    }

    /**
     * 根据供求类型分页获取供求信息
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse<JSONObject> getInfoListByType(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = mapper.countInfoListByType(jsonObject);
        List<JSONObject> list = mapper.listInfoByType(jsonObject);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, count), true);
    }

//    /**
//     * 分页获取供求信息
//     *
//     * @param body
//     * @return
//     */
//    public ObjectRestResponse listByPage(Map<String, Object> body) {
//        try {
//            Query query = new Query(body);
//            Example example = getExampleByQuery(query);
//            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
//            List<SupplyDemandInfo> list = selectByExample(example);
//            List<String> urlList;
//            for (SupplyDemandInfo supplyDemandInfo : list) {
//                int id = supplyDemandInfo.getId();
//                urlList = fileInfoBiz.getFileUrlList(CommonConstants.ULTABLE_GSDI, id);
//                supplyDemandInfo.setUrlList(urlList);
//            }
//            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
//        } catch (Exception ex) {
//            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
//        }
//    }
//
//    // 获取一条供求信息
//    public ObjectRestResponse getSupplyDemandById(int id) {
//        try {
//            SupplyDemandInfo supplyDemandInfo = selectById(id);
//            List<String> urlList;
//            if (supplyDemandInfo != null) {
//                urlList = fileInfoBiz.getFileUrlList(CommonConstants.ULTABLE_GSDI, id);
//                supplyDemandInfo.setUrlList(urlList);
//            }
//            return new ObjectRestResponse<>(StatusCode.SUCCESS, supplyDemandInfo);
//        } catch (Exception ex) {
//            return new ObjectRestResponse(StatusCode.FAIL);
//        }
//    }

    /**
     * 新增供求信息
     *
     * @param map
     * @param files
     * @return
     */
    public ObjectRestResponse addNewSupplyDemand(Map<String, String> map, MultipartFile[] files) {
        try {
            SupplyDemandInfo supplyDemandInfo = fillByMap(map);
            int affect = mapper.insertSelective(supplyDemandInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            int insertId = supplyDemandInfo.getId();
            // 上传文件,待完善。
            fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, insertId);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * 用户新增供求信息
     *
     * @param files
     * @return
     */
    public ObjectRestResponse addByUer(SupplyDemandInfo supplyDemandInfo, MultipartFile[] files) {
        try {
            int affect = mapper.insertSelective(supplyDemandInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            int insertId = supplyDemandInfo.getId();
            // 上传文件,待完善。
            fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, insertId);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    // 更新 供求信息
    public ObjectRestResponse updateSupplyDemand(SupplyDemandInfo supplyDemandInfo, Integer[] delIds, MultipartFile[] files) {
        //步骤一： 更新 供求信息
        int i = mapper.updateByPrimaryKeySelective(supplyDemandInfo);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FILEDELETEFAIL, false);
        }
        // 步骤二 ：删除旧图片文件&删除旧图片数据库
        if (delIds != null) {
            try {
                //删除数量
                int i1 = delImgByIds(delIds);
            } catch (Exception e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
            }
        }
        //新增新图片
        boolean suc = fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, supplyDemandInfo.getId());
        //存在文件且上传数量为0，返回异常
        if (files.length > 0 && !suc) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);

    }

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

    //根据 待删除图片数组ids，删除图片
    public int delImgByIds(Integer[] delIds) throws Exception {
        //int类型数组转为List，提供tk.mybatis in查询
        List<Integer> delIdList = new ArrayList<>();
        Collections.addAll(delIdList, delIds);
        //根据id查出相应的图片路径
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", delIdList);
        criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_GSDI);
        List<FileInfo> fileInfos = fileInfoBiz.selectByExample(example);
        //根据id删除数据库保存的信息
        batchDeleteById(delIdList);
        //根据List<FileInfo>，取出路径进行批量删除图片
        return new FileUtils().delFilesByPath(fileInfos);
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

    public SupplyDemandInfo fillByMap(Map<String, String> map) throws ParseException {
        SupplyDemandInfo supplyDemandInfo = new SupplyDemandInfo();
        if (map.containsKey("id")) {
            supplyDemandInfo.setId(Integer.valueOf(map.get("id")));
        }
        if (map.containsKey("title")) {
            supplyDemandInfo.setTitle(map.get("title"));
        }
        if (map.containsKey("contactPerson")) {
            supplyDemandInfo.setContactPerson(map.get("contactPerson"));
        }
        if (map.containsKey("phone")) {
            supplyDemandInfo.setPhone(map.get("phone"));
        }
        if (map.containsKey("area")) {
            supplyDemandInfo.setArea(map.get("area"));
        }
        if (map.containsKey("pubTime")) {
            supplyDemandInfo.setPubTime(simpleDateFormat.parse(map.get("pubTime")));
        }
        if (map.containsKey("pubType")) {
            supplyDemandInfo.setPubType(map.get("pubType"));
        }
        if (map.containsKey("status")) {
            supplyDemandInfo.setStatus(map.get("status"));
        }
        if (map.containsKey("description")) {
            supplyDemandInfo.setDescription(map.get("description"));
        }
        return supplyDemandInfo;
    }

    public ObjectRestResponse<JSONObject> all(int[] ints, Map<String, Object> params) {
        //获取本机ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port + SON_PATH;
        //查询企业名录
        try {
            Example example = new Example(SupplyDemandInfo.class);
            String pubType = null; // 供求类型
            String title = null;  //标题
            String contactPerson = null; //联系人
            String status = null;
            if (params.containsKey("pubType")) {
                pubType = (String) params.get("pubType");
                if (!pubType.equals("")) {
                    example.createCriteria().andEqualTo("pubType", pubType);
                }
            }
            if (params.containsKey("status")) {
                status = (String) params.get("status");
                if (!status.equals("")) {
                    example.createCriteria().andEqualTo("status", status);
                }
            }
            if (params.containsKey("title")) {
                title = "%" + params.get("title") + "%";
                if (!title.equals("%%")) {
                    example.createCriteria().andLike("title", title);
                }
            }
            if (params.containsKey("contactPerson")) {
                contactPerson = "%" + params.get("contactPerson") + "%";
                if (!contactPerson.equals("%%")) {
                    example.createCriteria().andLike("contactPerson", contactPerson);
                }
            }
            List<SupplyDemandAndFile> ProductsList = supplyDemandInfoMapper.listSupplyDemand(ints[0], ints[1], pubType, title, contactPerson, status);
            for (SupplyDemandAndFile supplyDemandAndFile : ProductsList) {
                //加上服務器ip和端口
                List<FileInfo> imgLists = supplyDemandAndFile.getImgLists();
                for (FileInfo img : imgLists) {
                    img.setPath(s + img.getPath());
                }
            }
            //查询总数
            example.createCriteria().andEqualTo("isDeleted", 0);
            int i = selectCountByExample(example);
            return new ObjectRestResponse<JSONObject>(StatusCode.SUCCESS, CommonUtil.successPage(ProductsList, i), true);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    // 根据id获取一条供求信息
    public ObjectRestResponse getOneById(Integer id) {
        String s = ip + ":" + port + SON_PATH;
        try {
            List<SupplyDemandAndFile> ProductsList = supplyDemandInfoMapper.getOneById(id);
            for (SupplyDemandAndFile supplyDemandAndFile : ProductsList) {
                //加上服務器ip和端口
                List<FileInfo> imgLists = supplyDemandAndFile.getImgLists();
                for (FileInfo img : imgLists) {
                    img.setPath(s + img.getPath());
                }
            }
            return new ObjectRestResponse(StatusCode.SUCCESS, ProductsList.get(0), true);

        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    public ObjectRestResponse<T> delOne(List<Integer> ids) {
        int i = 0;
        try {
            Example example = new Example(FileInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("tableId", ids);
            criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_GSDI);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
            //根据id删除中间表数据库保存的信息
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //根据List<FileInfo>，取出路径进行批量删除图片
            new FileUtils().delFilesByPath(fileInfos);
            //主表删除
            for (Integer id : ids) {
                i = supplyDemandInfoMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception e) {
            return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
        }
        if (i > 0) {
            return new ObjectRestResponse<T>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<T>(StatusCode.FAIL, false);
    }

    public ObjectRestResponse<HashMap<String, Object>> getPageByUserToken(Integer start, Integer size, Integer userId) {
        //分页查询信息
        List<SupplyDemandAndFile> supplyDemandAndFiles = supplyDemandInfoMapper.listSupplyByUser(start, size, userId);
        //总数
        Example example = new Example(SupplyDemandInfo.class);
        example.createCriteria().andEqualTo("isDeleted", 0).andEqualTo("userid", userId);
        int count = selectCountByExample(example);
        //合并
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", supplyDemandAndFiles);
        map.put("total", count);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
    }
}
