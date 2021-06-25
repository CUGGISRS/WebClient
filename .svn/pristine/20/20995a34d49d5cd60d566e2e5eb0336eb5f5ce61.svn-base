package com.github.wxiaoqi.security.zs.sys.biz;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.feign.service.IDeptTestService;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.mapper.HarvestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收获
 */
@Service
public class HarvestBiz extends BaseBiz<HarvestMapper, Harvest> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProcessBatchBiz processBatchBiz;
    @Autowired
    private BuyInfoBiz buyInfoBiz;
    @Autowired
    private FinishProductTestBiz finishProductTestBiz;
    @Autowired
    private IDeptTestService deptTestService;

    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;
    @Autowired
    private SaleBiz saleBiz;

    /**
     * 修改某一产品的数据
     */
    public int updateByPId(Harvest obj, Integer productId) {
        if (productId == null) {
            return 0;
        }
        String fieldName = "productId";
        return updateByFiled(obj, productId, fieldName);
    }

    /**
     * 通过产品id获得数据
     */
    public List<Harvest> getByPId(Integer productId) {
        if (productId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productId", productId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 通过产品id集合获得数据
     *
     * @param pIds
     * @return
     */
    public List<Harvest> getByPIds(List<Integer> pIds) {
        String propertyName = "productId";
        return getByInFiledMayToMap(pIds, propertyName, null);
    }


    /**
     * 获得追溯码获得一条数据
     */
    public Harvest getOneByTraceCode(String traceCode) throws Exception {
        if (MyObjectUtil.noNullOrEmpty(traceCode)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("traceCode", traceCode);
            List<Harvest> list = getByToMap(andToMap);
            int sum = MyObjectUtil.iterableCount(list);
            if (sum == 1) {
                return list.get(0);
            } else if (sum > 1) {
                throw new Exception("收获中追溯码必须唯一");
            }
        }
        return null;
    }

    /**
     * 获得追溯码集合获得数据
     */
    public List<Harvest> getByTraceCodes(List<String> traceCodes) {
        String inField = "traceCode";
        return getByInFiledMayToMap(traceCodes, inField, null);
    }


    /**
     * 通过id删除收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Harvest delLinkAndFile(Integer id) throws Exception {
        Harvest old = selectById(id);
        if (old != null) {
            int index = deleteById(id);
            if (index == 0) {
                throw new Exception("收购删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            old.setHarvestPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_12, delUrls));

            String traceCode = old.getTraceCode();
            //收购（带文件）
            List<BuyInfo> buyInfos = buyInfoBiz.getByTraceCode(traceCode);
            List<Integer> bIds = MyObjectUtil.getIdsByList(buyInfos);
            fileInfoService.commonDelByFIdsAndFName(bIds, CommonConstants.FORM_NAME_23, delUrls);
            buyInfoBiz.batchDeleteByIds(bIds);

            //加工（带文件） 收购
            List<ProcessBatch> processBatchesB = processBatchBiz.getByBIds(bIds);
            List<Integer> pbIdsB = MyObjectUtil.getIdsByList(processBatchesB);
            fileInfoService.commonDelByFIdsAndFName(pbIdsB, CommonConstants.FORM_NAME_14, delUrls);
            processBatchBiz.batchDeleteByIds(pbIdsB);
            //加工（带文件） 收获
            List<ProcessBatch> processBatches = processBatchBiz.getByHId(id);
            List<Integer> pbIds = MyObjectUtil.getIdsByList(processBatches);
            fileInfoService.commonDelByFIdsAndFName(pbIds, CommonConstants.FORM_NAME_14, delUrls);
            processBatchBiz.batchDeleteByIds(pbIds);


            List<String> tcs = new ArrayList<>();
            tcs.add(traceCode);
            //添加加工的追溯码
            addTCs(tcs, processBatches, processBatchesB);

            processBatchBiz.delCommon(tcs, delUrls);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);

        }
        return old;
    }

    /**
     * 通过id集合删除收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Integer delLinkAndFile(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<Harvest> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("收获批量删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_12, delUrls);
            List<String> traceCodes = getTraceCodesByList(list);

            delCommon(ids, traceCodes, delUrls);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);
            return sum;
        }
        return null;
    }

    private void addTCs(List<String> tcs, List<ProcessBatch> hpbS, List<ProcessBatch> bpbS) {
        List<String> btcList = processBatchBiz.getTraceCodesByList(bpbS);
        List<String> htcList = processBatchBiz.getTraceCodesByList(hpbS);
        if (btcList != null) {
            tcs.addAll(btcList);
        }
        if (htcList != null) {
            tcs.addAll(htcList);
        }
    }

    public void delCommon(List<Integer> ids, List<String> traceCodes, List<String> delUrls) throws Exception {

        //收购（带文件）
        List<BuyInfo> buyInfos = buyInfoBiz.getByTraceCodes(traceCodes);
        List<Integer> bIds = MyObjectUtil.getIdsByList(buyInfos);
        fileInfoService.commonDelByFIdsAndFName(bIds, CommonConstants.FORM_NAME_23, delUrls);
        buyInfoBiz.batchDeleteByIds(bIds);

        //加工（带文件） 收获
        List<ProcessBatch> processBatches = processBatchBiz.getByHIds(ids);
        List<Integer> pbIds = MyObjectUtil.getIdsByList(processBatches);
        fileInfoService.commonDelByFIdsAndFName(pbIds, CommonConstants.FORM_NAME_14, delUrls);
        processBatchBiz.batchDeleteByIds(pbIds);

        //加工（带文件） 收购
        List<ProcessBatch> processBatchesB = processBatchBiz.getByBIds(bIds);
        List<Integer> pbIdsB = MyObjectUtil.getIdsByList(processBatchesB);
        fileInfoService.commonDelByFIdsAndFName(pbIdsB, CommonConstants.FORM_NAME_14, delUrls);
        processBatchBiz.batchDeleteByIds(pbIdsB);

        List<String> tcs = new ArrayList<>();
        if (traceCodes != null) {
            tcs.addAll(traceCodes);
        }
        //添加加工的追溯码
        addTCs(tcs, processBatches, processBatchesB);

        processBatchBiz.delCommon(tcs, delUrls);

    }


    /**
     * 通过集合获得id集合
     */
    public List<Integer> getIdsByList(List<Harvest> list) {
        if (list != null) {
            List<Integer> ids = list.stream().map(Harvest::getId).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(ids) > 0) {
                return ids;
            }
        }
        return null;
    }

    /**
     * 通过集合获得追溯码集合
     */
    public List<String> getTraceCodesByList(List<Harvest> list) {
        if (list != null) {
            List<String> tcs = list.stream().map(Harvest::getTraceCode).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(tcs) > 0) {
                return tcs;
            }
        }
        return null;
    }
}
