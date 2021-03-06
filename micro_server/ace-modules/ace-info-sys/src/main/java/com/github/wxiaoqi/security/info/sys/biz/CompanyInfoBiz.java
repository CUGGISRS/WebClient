package com.github.wxiaoqi.security.info.sys.biz;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.info.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.info.sys.entity.CompanyAndFile;
import com.github.wxiaoqi.security.info.sys.entity.CompanyInfo;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import com.github.wxiaoqi.security.info.sys.mapper.CompanyInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.info.sys.utils.FileUtils;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyInfoBiz extends BaseBiz<CompanyInfoMapper, CompanyInfo> {

    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

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

    public ObjectRestResponse addNewCompanyInfo(Map<String, String> map, MultipartFile[] multipartFile) {
        try {
            CompanyInfo companyInfo = new CompanyInfo();
            // ????????????
            if (map.containsKey("companyName")) {
                companyInfo.setCompanyName(map.get("companyName"));
            }
            // ????????????
            if (map.containsKey("companyType")) {
                companyInfo.setCompanyType(map.get("companyType"));
            }
            if (map.containsKey("chargeMan")) {
                companyInfo.setChargeMan(map.get("chargeMan"));
            }
            if (map.containsKey("phone")) {
                companyInfo.setPhone(map.get("phone"));
            }
            if (map.containsKey("registMoney")) {
                companyInfo.setRegistMoney(map.get("registMoney"));
            }
            if (map.containsKey("businessCode")) {
                companyInfo.setBusinessCode(map.get("businessCode"));
            }
            if (map.containsKey("industry")) {
                companyInfo.setIndustry(map.get("industry"));
            }
            if (map.containsKey("introduction")) {
                companyInfo.setIntroduction(map.get("introduction"));
            }
            if (map.containsKey("place")) {
                companyInfo.setPlace(map.get("place"));
            }
            companyInfo.setIsDeleted(0);
            int affect = mapper.insertSelective(companyInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            int insertId = companyInfo.getId();
            // ????????????
            fileInfoBiz.addOneFile(multipartFile, CommonConstants.ULTABLE_ZCI, insertId);
            return new ObjectRestResponse(StatusCode.SUCCESS);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex.getMessage());
        }
    }

    // ??????????????????
    public ObjectRestResponse updateCompanyInfo(CompanyInfo companyInfo, Integer[] delIds, MultipartFile[] files) {
        try {
            //1.?????????????????????
            int i = mapper.updateByPrimaryKeySelective(companyInfo);
            if (i == 0) {
                return new ObjectRestResponse<>(StatusCode.FILEDELETEFAIL, false);
            }
            //2.?????????????????????&????????????????????????
            if (delIds != null) {
                try {
                    //????????????
                    int i1 = delImgByIds(delIds);
                } catch (Exception e) {
//                    return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
                }
            }
            //???????????????
            boolean suc = fileInfoBiz.addOneFile(files, CommonConstants.ULTABLE_ZCI, companyInfo.getId());
            //??????????????????????????????0???????????????
            if (files.length > 0 && !suc) {
                return new ObjectRestResponse<>(StatusCode.FAIL, false);
            }
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);

        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
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
        criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_ZCI);
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
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
            FileInfo productBaseImg = new FileInfo();
            //?????????id
            productBaseImg.setTableId(primaryKey);
            productBaseImg.setLinkTable(linkTable);
            //???????????? ??????ip + ?????? + (????????? + ?????????)????????? +?????????
            //productBaseImg.setPath(host + ":" + port + SON_PATH + imageName);
            //???????????????????????????
            productBaseImg.setPath(SON_PATH + imageName);
            insert += fileInfoMapper.insertSelective(productBaseImg);
            System.out.println("????????????");
        }
        return insert;
    }

    /**
     * ??????????????????????????????
     *
     * @param params
     * @return
     */
    public ObjectRestResponse listByPage(Map<String, Object> params) {
        try {
            Query query = new Query(params);
            Example example = getExampleByQuery(query);
            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<CompanyInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    public ObjectRestResponse<JSONObject> all(int[] ints, String companyName) {
        //????????????ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        //??????????????????
        try {
            Integer page = null;
            Integer size = null;
            if (ints != null) {
                page = ints[0];
                size = ints[1];
            }
            List<CompanyAndFile> ProductsList = companyInfoMapper.listCompany(page, size, companyName);
            for (CompanyAndFile companyAndFile : ProductsList) {
                //???????????????ip?????????
                List<FileInfo> imgLists = companyAndFile.getImgLists();
                for (FileInfo img : imgLists) {
                    img.setPath(s + img.getPath());
                }
            }
            //????????????
            Example example = new Example(CompanyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isDeleted", 0);
            if (companyName != null && !"".equals(companyName)) {
                //???????????????????????????
                String sql = " instr(company_name,'" + companyName + "' )>0";
                criteria.andCondition(sql);
                //   criteria.andLike("","%" + companyName + "%");
            }
            int i = selectCountByExample(example);
            return new ObjectRestResponse<JSONObject>(StatusCode.SUCCESS, CommonUtil.successPage(ProductsList, i), true);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    public ObjectRestResponse<CompanyAndFile> queryById(Integer id) {
        //???????????????????????????
        CompanyInfo companyInfo = selectById(id);
        if (companyInfo == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //??????????????????
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_ZCI);
        criteria.andEqualTo("tableId", id);
        //??????list
        List<FileInfo> ImgLists = fileInfoMapper.selectByExample(example);
        //???????????????ip?????????
        addHostAndPort(ImgLists);
        //bean??????
        CompanyAndFile companyAndFile = new CompanyAndFile();
        BeanUtil.copyProperties(companyInfo, companyAndFile);
        companyAndFile.setImgLists(ImgLists);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, companyAndFile);
    }

    //????????????ip?????????
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

    public ObjectRestResponse<T> delCompany(Integer[] ids) {
        int i = 0;
        try {
            Example example = new Example(FileInfo.class);
            Example.Criteria criteria = example.createCriteria();
            List<Integer> delIdList = new ArrayList<>();
            Collections.addAll(delIdList, ids);
            criteria.andIn("tableId", delIdList);
            criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_ZCI);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
            //??????id???????????????????????????????????????
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //??????List<FileInfo>???????????????????????????????????????
            new FileUtils().delFilesByPath(fileInfos);
            //????????????
            for (Integer id : ids) {
                i = companyInfoMapper.deleteByPrimaryKey(id);
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
