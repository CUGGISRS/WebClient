package com.github.wxiaoqi.security.dzsw.sys.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.dzsw.sys.entity.FileInfo;
import com.github.wxiaoqi.security.dzsw.sys.entity.QualityProductsAndFile;
import com.github.wxiaoqi.security.dzsw.sys.entity.QualityProductsInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.mapper.QualityProductsInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class QualityProductsInfoBiz extends BaseBiz<QualityProductsInfoMapper, QualityProductsInfo> {

    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Resource
    private FileInfoMapper fileInfoMapper;

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

    @Resource
    private QualityProductsInfoMapper qualityProductsInfoMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ObjectRestResponse getInfoById(int id) {
        try {
            QualityProductsInfo qualityProductsInfo = selectById(id);
            addOneReading(id);

            //????????????
            String s = ip + ":" + port;
            //???????????????ip?????????
            List<FileInfo> imgLists = fileInfoBiz.getByTableIdAndLinkTable(id,CommonConstants.ULTABLE_DQPI);
            if (imgLists != null) {
                for (FileInfo img : imgLists) {
                    img.setPath(s + img.getPath());
                }
            }
            QualityProductsAndFile qualityProductsAndFile=new  QualityProductsAndFile();
            BeanUtil.copyProperties(qualityProductsInfo, qualityProductsAndFile);
            qualityProductsAndFile.setImgLists(imgLists);
            return new ObjectRestResponse(StatusCode.SUCCESS, qualityProductsAndFile);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * ?????????????????????????????????1
     *
     * @param id
     * @return
     */
    public void addOneReading(int id) {
        QualityProductsInfo qualityProductsInfo = selectById(id);
        try {
            // ????????????popularity???????????????null
            if (qualityProductsInfo.getPopularity() == null) {
                qualityProductsInfo.setPopularity(1);
            } else {
                qualityProductsInfo.setPopularity(qualityProductsInfo.getPopularity() + 1);
            }
            updateSelectiveById(qualityProductsInfo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse getMostPopularityProduct(JSONObject jsonObject) {
        try {
            if (jsonObject.containsKey("limit")) {
                jsonObject.put("limit", jsonObject.getIntValue("limit"));
            } else {
                // ???????????????????????????????????????10????????????????????????
                jsonObject.put("limit", 10);
            }
            List<QualityProductsAndFile> list = mapper.getMostPopularityProducts(jsonObject.getIntValue("limit"));
//            String host = null;
//            try {
//                host = InetAddress.getLocalHost().getHostAddress();
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
            String s = ip + ":" + port;
            for (QualityProductsAndFile qualityProductsAndFile : list) {
                //???????????????ip?????????
                List<FileInfo> imgLists = qualityProductsAndFile.getImgLists();
                if (imgLists != null) {
                    for (FileInfo img : imgLists) {
                        img.setPath(s + img.getPath());
                    }
                }
            }
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, list.size()), true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }
    }


    // ???????????????????????????
    public TableResultResponse listByType(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        Example example = new Example(QualityProductsInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (jsonObject.containsKey("category")) {
            criteria.andEqualTo("productCategory", jsonObject.getString("category"));
        }
        if (jsonObject.containsKey("status")) {
            // ???????????????status?????????????????????status ?????????
            criteria.andEqualTo("status", jsonObject.getString("status"));
        } else {
            // ????????????????????? status ?????????????????????????????????????????????????????????
            criteria.andEqualTo("status", CommonConstants.EX_PASSED);
        }
        criteria.andEqualTo("isDeleted", 0);
        Page<Object> result = PageHelper.startPage(jsonObject.getIntValue("pageNum"), jsonObject.getIntValue("pageRow"));
        List<QualityProductsInfo> list = selectByExample(example);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    public QualityProductsInfo fillByMap(Map<String, String> map) {

        QualityProductsInfo qualityProductsInfo = new QualityProductsInfo();
        if (map.containsKey("id")) {
            qualityProductsInfo.setId(Integer.valueOf(map.get("id")));
        }
        if (map.containsKey("productName")) {
            qualityProductsInfo.setProductName(map.get("productName"));
        }
        if (map.containsKey("productCategory")) {
            qualityProductsInfo.setProductCategory(map.get("productCategory"));
        }
        if (map.containsKey("price")) {
            BigDecimal price = new BigDecimal(map.get("price"));
            qualityProductsInfo.setPrice(price);
        }
        if (map.containsKey("description")) {
            qualityProductsInfo.setDescription(map.get("description"));
        }
        if (map.containsKey("productOrigin")) {
            qualityProductsInfo.setProductOrigin(map.get("productOrigin"));
        }
        if (map.containsKey("purchaseWay")) {
            qualityProductsInfo.setPurchaseWay(map.get("purchaseWay"));
        }
        if (map.containsKey("status")) {
            qualityProductsInfo.setStatus(map.get("status"));
        }
        qualityProductsInfo.setPopularity(0);
        if (map.containsKey("referrerid")) {
            qualityProductsInfo.setReferrerid(Integer.valueOf(map.get("referrerid")));
        }
        if (map.containsKey("referrer")) {
            qualityProductsInfo.setReferrer(map.get("referrer"));
        }
        return qualityProductsInfo;
    }

    /**
     * ?????????????????????
     *
     * @param map
     * @param files
     * @return
     */
    public ObjectRestResponse addNewQualityProduct(Map<String, String> map, MultipartFile[] files) {
        try {
            Date curDate = new Date();
            QualityProductsInfo qualityProductsInfo = fillByMap(map);
            // ???????????????????????????
            qualityProductsInfo.setPubTime(curDate);
            qualityProductsInfo.setUptTime(curDate);
            qualityProductsInfo.setIsDeleted(0);
            int affect = mapper.insertSelective(qualityProductsInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            int insertId = qualityProductsInfo.getId();
            // ????????????
            fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_DQPI, insertId);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * ?????????????????????
     *
     * @param map
     * @return
     */
    public ObjectRestResponse updateQualityProduct(Map<String, String> map, Integer[] delIds, MultipartFile[] files) {
        try {
            Date curDate = new Date();
            QualityProductsInfo qualityProductsInfo = fillByMap(map);
            // ????????????
            qualityProductsInfo.setPubTime(simpleDateFormat.parse(map.get("pubTime")));
            // ????????????
            qualityProductsInfo.setUptTime(curDate);
            qualityProductsInfo.setIsDeleted(0);
            int affect = updateById(qualityProductsInfo);
            if (affect == 0)
                return new ObjectRestResponse<>(StatusCode.FAIL);

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
            boolean suc = fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_DQPI, qualityProductsInfo.getId());
            //??????????????????????????????0???????????????
            if (files.length > 0 && !suc) {
                return new ObjectRestResponse<>(StatusCode.FAIL, false);
            }
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
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
        criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_DQPI);
        List<FileInfo> fileInfos = fileInfoBiz.selectByExample(example);
        //??????id??????????????????????????????
        batchDeleteById(delIdList);
        //??????List<FileInfo>???????????????????????????????????????
        return delFilesByPath(fileInfos);
    }

    public int delFilesByPath(List<FileInfo> fileInfos) {
        int i = 0;
        for (FileInfo fileinfo : fileInfos) {
            File file = new File(fileinfo.getPath());
            // ????????????????????????????????????
            if (FileUtil.isFile(file)) {
                if (FileUtil.del(file)) {
                    i++;
                    System.out.println("????????????");
                }
            }
        }
        return i;
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

    /**
     * ???????????????????????????
     *
     * @return
     */
    public ObjectRestResponse listByPage(int[] ints, Map<String, Object> params) {
        //????????????ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        try {
            //????????????
            Example example = new Example(QualityProductsInfo.class);
            String productCategory = null; // ????????????
            String status = null;  // ????????????
            String productName = null; // ???????????????
            if (params.containsKey("productCategory")) {
                productCategory = (String) params.get("productCategory");
                if (!productCategory.equals("")) {
                    example.createCriteria().andEqualTo("productCategory", productCategory);
                }
            }
            if (params.containsKey("status")) {
                status = (String) params.get("status");
            }
            if (params.containsKey("productName")) {
                productName = "%" + params.get("productName") + "%";
                if (!productName.equals("%%")) {
                    example.createCriteria().andLike("productName", productName);
                }
            }
            List<QualityProductsAndFile> qualityProductsInfoList = qualityProductsInfoMapper.listQualityProducts(ints[0], ints[1], productCategory, status, productName);
            for (QualityProductsAndFile qualityProductsAndFile : qualityProductsInfoList) {
                //???????????????ip?????????
                List<FileInfo> imgLists = qualityProductsAndFile.getImgLists();
                if (imgLists != null) {
                    for (FileInfo img : imgLists) {
                        img.setPath(s + img.getPath());
                    }
                }
            }
            example.createCriteria().andEqualTo("isDeleted", 0);
            int i = selectCountByExample(example);
            return new ObjectRestResponse<JSONObject>(StatusCode.SUCCESS, CommonUtil.successPage(qualityProductsInfoList, i), true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

    /**
     * ???????????????????????????url??????
     *
     * @param list
     * @return
     */
    public List<QualityProductsInfo> getUrlList(List<QualityProductsInfo> list) {
        for (QualityProductsInfo qualityProductsInfo : list) {
            int id = qualityProductsInfo.getId();
            List<String> urlList = fileInfoBiz.urlListByTableAndId(CommonConstants.ULTABLE_DQPI, id);
            qualityProductsInfo.setUrlList(urlList);
        }
        return list;
    }

    // ???????????????????????????
    public ObjectRestResponse<T> delOne(List<Integer> ids) {
        int i = 0;
        try {
            Example example = new Example(FileInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("tableId", ids);
            criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_DQPI);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
            //??????id???????????????????????????????????????
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //??????List<FileInfo>???????????????????????????????????????
            delFilesByPath(fileInfos);
            //????????????
            for (Integer id : ids) {
                i = qualityProductsInfoMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception e) {
            return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
        }
        if (i > 0) {
            return new ObjectRestResponse<T>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<T>(StatusCode.FAIL, false);
    }
}
