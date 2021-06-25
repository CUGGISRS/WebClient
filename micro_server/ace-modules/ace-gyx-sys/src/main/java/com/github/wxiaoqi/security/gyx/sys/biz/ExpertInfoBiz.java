package com.github.wxiaoqi.security.gyx.sys.biz;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.gyx.sys.entity.ExpertInfo;
import com.github.wxiaoqi.security.gyx.sys.mapper.ExpertInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.info.sys.utils.FileUtils;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpertInfoBiz extends BaseBiz<ExpertInfoMapper, ExpertInfo> {
    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Resource
    ExpertInfoMapper expertInfoMapper;

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

    public ObjectRestResponse<JSONObject> getExpertInfoList(int[] ints,
                                                            String serviceArea,
                                                            String expertName,
                                                            String expertise) {
        int count = expertInfoMapper.countExpertInfoList(expertName,serviceArea,expertise);
        List<JSONObject> list = expertInfoMapper.listExpertInfo(ints[0], ints[1], expertName, serviceArea,expertise);
        //处理图片url
        for (JSONObject obj : list) {
            if (!ObjectUtil.isNull(obj.get("photo"))) {
                obj.put("photo", "http://" + ip + ":" + port + SON_PATH + obj.get("photo"));
            }
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, count), true);
    }

//    // 分页获取专家信息
//    public ObjectRestResponse getExpertInfoListByPage(Map<String, Object> params) {
//        try {
//            Query query = new Query(params);
//            Example example = getExampleByQuery(query);
//            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
//            List<ExpertInfo> list = selectByExample(example);
//            //处理图片url
//            for (ExpertInfo expertInfo : list) {
//                expertInfo.setPhoto(CommonConstants.serverIp + ":" + port + expertInfo.getPhoto());
//            }
//            return new ObjectRestResponse(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
//        } catch (Exception ex) {
//            return new ObjectRestResponse(StatusCode.FAIL);
//        }
//    }


    public ObjectRestResponse<ExpertInfo> getOne(Integer id) {
        ExpertInfo expertInfo = expertInfoMapper.selectByPrimaryKey(id);
        if (null == expertInfo) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //处理图片url
        expertInfo.setPhoto("http://" + ip + ":" + port + SON_PATH + expertInfo.getPhoto());
        return new ObjectRestResponse<>(StatusCode.SUCCESS, expertInfo, true);
    }

    public ObjectRestResponse<ExpertInfo> add(ExpertInfo expertInfo, MultipartFile file) {
        //1.上传图片
        if (file != null) {
            //根路径 + 子目录 "F:" + "/infoSys/"
            // 获取当前年月
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            String path = ROOT_PATH + SON_PATH + YMPath;
            String filePath;
            try {
                filePath = UploadFile.uploadFile(file, path);
            } catch (IOException e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "图片上传失败" + e.getMessage());
            }
            //2.存图片到数据库
            expertInfo.setPhoto(YMPath + filePath);
        }
        int insert = expertInfoMapper.insertSelective(expertInfo);
        if (insert == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<ExpertInfo> update(ExpertInfo expertInfo, MultipartFile file) {
        //如果有新图片，则替换旧图
        if (file != null) {
            //删除旧图
            ExpertInfo expertInfoOld = expertInfoMapper.selectByPrimaryKey(expertInfo.getId());
            if (null == expertInfoOld) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "信息不存在");
            }
            String photo = expertInfoOld.getPhoto();
            //旧图路径为空，跳过下列方法
            if (!StrUtil.isEmpty(photo)) {
                new FileUtils().delFilesByPath(ROOT_PATH + photo);
            }
            // 上传图片
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            String path = ROOT_PATH + SON_PATH + YMPath;
            String filePath;
            try {
                filePath = UploadFile.uploadFile(file, path);
            } catch (IOException e) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "图片上传失败" + e.getMessage());
            }
            expertInfo.setPhoto(YMPath + filePath);
        }
        int i = expertInfoMapper.updateByPrimaryKeySelective(expertInfo);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> delOne(List<Integer> ids) {
        String photo = null;
        boolean b;
        int delete = 0;
        for (Integer id : ids) {
            ExpertInfo expertInfo = expertInfoMapper.selectByPrimaryKey(id);
            if (null == expertInfo) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "信息不存在");
            }
            photo = expertInfo.getPhoto();
            //1.路径存在则，删除磁盘中的文件
            if (!StrUtil.hasEmpty(photo)) {
                b = new FileUtils().delFilesByPath(ROOT_PATH + SON_PATH + photo);
//                if (!b) {
//                    return new ObjectRestResponse<>(StatusCode.FAIL, false);
//                }
            }
            //2.删除数据库
            delete = expertInfoMapper.deleteByPrimaryKey(id);
            if (delete == 0) {
                return new ObjectRestResponse<>(StatusCode.FAIL, false);
            }

        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }
}
