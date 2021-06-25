package com.github.wxiaoqi.security.zs.sys.biz;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.zs.sys.entity.BuyInfo;
import com.github.wxiaoqi.security.zs.sys.entity.ProcessBatch;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.mapper.BuyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 收购
 */
@Service
public class BuyInfoBiz extends BaseBiz<BuyInfoMapper, BuyInfo> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProcessBatchBiz processBatchBiz;

    /**
     * 修改某一企业的数据
     */
    public int updateByEId(BuyInfo obj, Integer eId) {
        if (eId == null) {
            return 0;
        }
        String fieldName = "enterpriseId";
        return updateByFiled(obj, eId, fieldName);
    }

    /**
     * 修改某些追溯码的数据
     */
    public int updateByTraceCodes(BuyInfo obj, List<String> traceCodes) {
        String fieldName = "traceCode";
        return updateByInFiledMayToMap(obj, traceCodes, fieldName, null);
    }

    /**
     * 获得某一企业(某一系统模块)(收购时间大于等于before，小于等于after)的数据
     */
    public List<BuyInfo> getByEId(Object enterpriseId, Object sysModule, Date before, Date after) {
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);
            toMap.put("sysModule", sysModule);
            String fieldName = "buyDate";
            Map<String, Object> gtMap = new HashMap<>();
            gtMap.put(fieldName, before);
            Map<String, Object> ltMap = new HashMap<>();
            ltMap.put(fieldName, after);
            return getMayCondition(null, null, null, null, null, null,
                    toMap, null, null, gtMap, null, ltMap, null,
                    null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null);
        }
        return null;
    }


    /**
     * 通过id删除收购(带文件)、加工（带文件）
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public BuyInfo delLinkAndFile(Integer id) throws Exception {
        BuyInfo old = selectById(id);
        if (old != null) {
            int index = deleteById(id);
            if (index == 0) {
                throw new Exception("收购删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            List<FileInfo> fileInfos = fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_23, delUrls);
            setFiles(old, fileInfos);//回显文件

            //加工（带文件）
            List<ProcessBatch> processBatches = processBatchBiz.getByBId(id);
            List<Integer> pbIds = MyObjectUtil.getIdsByList(processBatches);
            fileInfoService.commonDelByFIdsAndFName(pbIds, CommonConstants.FORM_NAME_14, delUrls);
            processBatchBiz.batchDeleteByIds(pbIds);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);

        }
        return old;
    }

    /**
     * 通过id集合删除收购(带文件)、加工（带文件）
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Integer delLinkAndFile(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<BuyInfo> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("收购批量删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_23, delUrls);

            //加工（带文件）
            List<ProcessBatch> processBatches = processBatchBiz.getByBIds(ids);
            List<Integer> pbIds = MyObjectUtil.getIdsByList(processBatches);
            fileInfoService.commonDelByFIdsAndFName(pbIds, CommonConstants.FORM_NAME_14, delUrls);
            processBatchBiz.batchDeleteByIds(pbIds);

            fileInfoService.delFileByUrls(delUrls);
            return sum;
        }
        return null;
    }


    /**
     * 通过对象集合获得id集合
     */
    public List<Integer> getIdsByList(List<BuyInfo> list) {
        if (list != null) {
            List<Integer> ids = list.stream()
                    .map(BuyInfo::getId).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(ids) > 0) {
                return ids;
            }
        }
        return null;
    }

    /**
     * 通过对象集合获得追溯码集合
     */
    public List<String> getTCsByList(List<BuyInfo> list) {
        if (list != null) {
            List<String> ids = list.stream().map(BuyInfo::getTraceCode)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(ids) > 0) {
                return ids;
            }
        }
        return null;
    }


    /**
     * 通过追溯码获得数据
     */
    public List<BuyInfo> getByTraceCode(String traceCode) {
        if (StringHelper.isNullOrEmpty(traceCode)) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("traceCode", traceCode);
        return getByToMap(toMap);
    }


    /**
     * 通过追溯码集合获得数据
     */
    public List<BuyInfo> getByTraceCodes(List<String> traceCodes) {
        String propertyName = "traceCode";
        return getByInFiledMayToMap(traceCodes, propertyName, null);
    }

    /**
     * 设置文件属性值
     */
    public void setFiles(BuyInfo obj, List<FileInfo> fileInfos) {
        if (obj != null && fileInfos != null && fileInfos.size() > 0) {
            obj.setBuyPictureList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_1.equals(v.getType())
            ).collect(Collectors.toList()));
        }
    }
}
