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

    //?????????
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    //?????????
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
     * ??????????????????????????????????????????
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
//     * ????????????????????????
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
//    // ????????????????????????
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
     * ??????????????????
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
            // ????????????,????????????
            fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, insertId);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * ????????????????????????
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
            // ????????????,????????????
            fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, insertId);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    // ?????? ????????????
    public ObjectRestResponse updateSupplyDemand(SupplyDemandInfo supplyDemandInfo, Integer[] delIds, MultipartFile[] files) {
        //???????????? ?????? ????????????
        int i = mapper.updateByPrimaryKeySelective(supplyDemandInfo);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FILEDELETEFAIL, false);
        }
        // ????????? ????????????????????????&????????????????????????
        if (delIds != null) {
            try {
                //????????????
                int i1 = delImgByIds(delIds);
            } catch (Exception e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
            }
        }
        //???????????????
        boolean suc = fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_GSDI, supplyDemandInfo.getId());
        //??????????????????????????????0???????????????
        if (files.length > 0 && !suc) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);

    }

    public int saveMultipartFile(MultipartFile[] files, String path, String linkTable, Integer primaryKey) {
        String imageName = null;
        int insert = 0;
        for (MultipartFile file : files) {
            // ????????????????????????????????????????????????????????????????????????
            try {
                imageName = UploadFile.uploadFile(file, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileInfo fileInfo = new FileInfo();
            //?????????id
            fileInfo.setTableId(primaryKey);
            fileInfo.setLinkTable(linkTable);
            //???????????? ??????ip + ?????? + (????????? + ?????????)????????? +?????????
            //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
            //???????????????????????????
            fileInfo.setPath(SON_PATH + imageName);
            insert += fileInfoMapper.insertSelective(fileInfo);
            System.out.println("????????????");
        }
        return insert;
    }

    //?????? ?????????????????????ids???????????????
    public int delImgByIds(Integer[] delIds) throws Exception {
        //int??????????????????List?????????tk.mybatis in??????
        List<Integer> delIdList = new ArrayList<>();
        Collections.addAll(delIdList, delIds);
        //??????id???????????????????????????
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", delIdList);
        criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_GSDI);
        List<FileInfo> fileInfos = fileInfoBiz.selectByExample(example);
        //??????id??????????????????????????????
        batchDeleteById(delIdList);
        //??????List<FileInfo>???????????????????????????????????????
        return new FileUtils().delFilesByPath(fileInfos);
    }

    //????????????
    public int batchDeleteById(List<Integer> ids) throws Exception {
        if (ids == null) {
            return 0;
        }
        for (Integer id : ids) {
            int index = fileInfoMapper.deleteByPrimaryKey(id);
            if (index == 0) {
                throw new Exception("id???" + id + "?????????????????????");
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
        //????????????ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port + SON_PATH;
        //??????????????????
        try {
            Example example = new Example(SupplyDemandInfo.class);
            String pubType = null; // ????????????
            String title = null;  //??????
            String contactPerson = null; //?????????
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
                //???????????????ip?????????
                List<FileInfo> imgLists = supplyDemandAndFile.getImgLists();
                for (FileInfo img : imgLists) {
                    img.setPath(s + img.getPath());
                }
            }
            //????????????
            example.createCriteria().andEqualTo("isDeleted", 0);
            int i = selectCountByExample(example);
            return new ObjectRestResponse<JSONObject>(StatusCode.SUCCESS, CommonUtil.successPage(ProductsList, i), true);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    // ??????id????????????????????????
    public ObjectRestResponse getOneById(Integer id) {
        String s = ip + ":" + port + SON_PATH;
        try {
            List<SupplyDemandAndFile> ProductsList = supplyDemandInfoMapper.getOneById(id);
            for (SupplyDemandAndFile supplyDemandAndFile : ProductsList) {
                //???????????????ip?????????
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
            //??????id???????????????????????????????????????
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //??????List<FileInfo>???????????????????????????????????????
            new FileUtils().delFilesByPath(fileInfos);
            //????????????
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
        //??????????????????
        List<SupplyDemandAndFile> supplyDemandAndFiles = supplyDemandInfoMapper.listSupplyByUser(start, size, userId);
        //??????
        Example example = new Example(SupplyDemandInfo.class);
        example.createCriteria().andEqualTo("isDeleted", 0).andEqualTo("userid", userId);
        int count = selectCountByExample(example);
        //??????
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", supplyDemandAndFiles);
        map.put("total", count);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
    }
}
