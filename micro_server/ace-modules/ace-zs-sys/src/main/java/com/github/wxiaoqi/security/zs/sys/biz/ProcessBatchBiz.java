package com.github.wxiaoqi.security.zs.sys.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.URLUtil;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.feign.service.IDeptTestService;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.mapper.ProcessBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 加工批次
 */
@Service
public class ProcessBatchBiz extends BaseBiz<ProcessBatchMapper, ProcessBatch> {
    @Autowired
    private IFileInfoService fileInfoService;

    @Autowired
    private HarvestBiz harvestBiz;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private BaseInfoBiz baseInfoBiz;
    @Autowired
    private BaseWorkBiz baseWorkBiz;
    @Autowired
    private FinishProductTestBiz finishProductTestBiz;
    @Autowired
    private SaleBiz saleBiz;
    @Autowired
    private ProductDetailBiz productDetailBiz;
    @Autowired
    private BuyInfoBiz buyInfoBiz;
    @Autowired
    private DeviceSensorBiz deviceSensorBiz;
    @Autowired
    private DeviceSensorDataBiz deviceSensorDataBiz;
    @Autowired
    private GrowEnvironmentBiz growEnvironmentBiz;
    @Autowired
    private EnterpriseBiz enterpriseBiz;

    @Autowired
    private IDeptTestService deptTestService;
    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;


    /**
     * 修改某一加工基地的数据
     */
    public int updateByBaseId(ProcessBatch obj, Integer baseId) {
        if (baseId == null) {
            return 0;
        }
        String fieldName = "baseId";
        return updateByFiled(obj, baseId, fieldName);
    }

    /**
     * 通过收获id获得数据
     */
    public List<ProcessBatch> getByHId(Integer hId) {
        if (hId == null) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("harvestId", hId);
        return getByToMap(toMap);
    }

    /**
     * 通过收获id集合获得数据
     */
    public List<ProcessBatch> getByHIds(List<Integer> hIds) {
        String propertyName = "harvestId";
        return getByInFiledMayToMap(hIds, propertyName, null);
    }

    /**
     * 通过收购id获得数据
     */
    public List<ProcessBatch> getByBId(Integer bId) {
        if (bId == null) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("buyId", bId);
        return getByToMap(toMap);
    }

    /**
     * 通过收购id集合获得数据
     */
    public List<ProcessBatch> getByBIds(List<Integer> bIds) {
        String propertyName = "buyId";
        return getByInFiledMayToMap(bIds, propertyName, null);
    }


    /**
     * 获得产品批次号获得一条数据
     */
    public ProcessBatch getOneByPBN(String productBatchNumber) throws Exception {
        if (MyObjectUtil.noNullOrEmpty(productBatchNumber)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productBatchNumber", productBatchNumber);
            List<ProcessBatch> processBatches = getByToMap(andToMap);
            int sum = MyObjectUtil.iterableCount(processBatches);
            if (sum == 1) {
                return processBatches.get(0);
            } else if (sum > 1) {
                throw new Exception("加工批次中产品批次号必须唯一");
            }
        }
        return null;
    }

    /**
     * 获得产品批次号集合获得数据
     */
    public List<ProcessBatch> getByPBNs(List<String> pbnS) {
        String inField = "productBatchNumber";
        return getByInFiledMayToMap(pbnS, inField, null);
    }

    /**
     * 通过集合获得追溯码（产品批次号）集合
     */
    public List<String> getTraceCodesByList(List<ProcessBatch> list) {
        if (list != null) {
            List<String> tcs = list.stream().map(ProcessBatch::getProductBatchNumber).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(tcs) > 0) {
                return tcs;
            }
        }
        return null;
    }


    /**
     * 通过id删除加工(带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ProcessBatch delLinkAndFile(Integer id) throws Exception {
        ProcessBatch old = selectById(id);
        if (old != null) {
            int index = deleteById(id);
            if (index == 0) {
                throw new Exception("加工批次删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            old.setProductBatchNumberPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_14, delUrls));

            String traceCode = old.getProductBatchNumber();

            //成品检验（带文件）
            List<FinishProductTest> fptList = finishProductTestBiz.getByPBN(traceCode);
            List<Integer> fptIds = MyObjectUtil.getIdsByList(fptList);
            fileInfoService.commonDelByFIdsAndFName(fptIds, CommonConstants.FORM_NAME_10, delUrls);
            finishProductTestBiz.batchDeleteByIds(fptIds);

            //部门抽检（带文件）
            List<Integer> dtIds = deptTestService.getIdsByTraceCode(traceCode);
            fileInfoService.commonDelByFIdsAndFName(dtIds, CommonConstants.FORM_NAME_3, delUrls);
            deptTestService.delByIds(dtIds);

            delCommon0(fptIds, dtIds);

            //销售
            List<Sale> sales = saleBiz.getByPBN(traceCode);
            List<Integer> sIds = MyObjectUtil.getIdsByList(sales);
            saleBiz.batchDeleteByIds(sIds);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);

        }
        return old;
    }

    /**
     * 通过id集合删除加工(带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Integer delLinkAndFile(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<ProcessBatch> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("加工批次批量删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_14, delUrls);

            List<String> traceCodes = getTraceCodesByList(list);

            delCommon(traceCodes, delUrls);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);
            return sum;
        }
        return null;
    }

    public void delCommon0(List<Integer> fptIds, List<Integer> dtIds) throws Exception {

        //删除检验_项目 成品检验
        List<BetweenTestItem> betweenTestItems1 = betweenTestItemBiz.getByTIdsAndTType(fptIds, CommonConstants.TEST_TYPE_2);
        betweenTestItemBiz.delByItems(betweenTestItems1);
        //删除检验_项目 部门抽检
        List<BetweenTestItem> betweenTestItems2 = betweenTestItemBiz.getByTIdsAndTType(dtIds, CommonConstants.TEST_TYPE_3);
        betweenTestItemBiz.delByItems(betweenTestItems2);

    }

    public void delCommon(List<String> traceCodes, List<String> delUrls) throws Exception {
        //成品检验（带文件）
        List<FinishProductTest> fptList = finishProductTestBiz.getByPBNs(traceCodes);
        List<Integer> fptIds = MyObjectUtil.getIdsByList(fptList);
        fileInfoService.commonDelByFIdsAndFName(fptIds, CommonConstants.FORM_NAME_10, delUrls);
        finishProductTestBiz.batchDeleteByIds(fptIds);

        //部门抽检（带文件）
        List<Integer> dtIds = deptTestService.getIdsByTraceCodes(traceCodes);
        fileInfoService.commonDelByFIdsAndFName(dtIds, CommonConstants.FORM_NAME_3, delUrls);
        deptTestService.delByIds(dtIds);

        delCommon0(fptIds, dtIds);
        //销售
        List<Sale> sales = saleBiz.getByPBNs(traceCodes);
        List<Integer> sIds = MyObjectUtil.getIdsByList(sales);
        saleBiz.batchDeleteByIds(sIds);
    }


    /**
     * 通过产品批次号（或追溯码）获得追溯信息（产品、基地、基本作业、收获、加工批次、成品检验、销售、产品详情、收购、传感器）（带文件）
     */
    public JSONObject traceByPBN(String productBatchNumber) throws Exception {
        JSONObject obj = new JSONObject(true);
        //加工批次
        ProcessBatch processBatch = getOneByPBN(productBatchNumber);
        //销售
        List<Sale> sales = saleBiz.getByPBN(productBatchNumber);

        //成品检验
        List<FinishProductTest> finishProductTests = finishProductTestBiz.getByPBN(productBatchNumber);
        if (finishProductTests != null) {
            List<Integer> ids = finishProductTests.stream().map(FinishProductTest::getId).collect(Collectors.toList());
            //设置检验报告文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_10);
            FileInfoUtil.setListField("setTestResultPictureList", fileInfos, finishProductTests,
                    CommonConstants.FILE_TYPE_1);
        }

        Harvest harvest;
        Product product = null;
        List<BaseWork> baseWorks = null;
        BaseInfo baseInfo = null;
        BaseInfo pBaseIfo = null;

        BuyInfo buyInfo = null;
        ProductDetail productDetail;
        Integer pDId = null;
        List<DeviceSensor> deviceSensors = null;
        //  String wlToken = null;

        if (processBatch != null) {
            //设置加工批次文件
            processBatch.setProductBatchNumberPictureList(
                    fileInfoService.getByFormIdAndFormName(processBatch.getId(), CommonConstants.FORM_NAME_14));

            //产品详情（加工）
            pDId = processBatch.getProductDetailId();

            //加工基地
            Integer pBaseId = processBatch.getBaseId();
            pBaseIfo = baseInfoBiz.selectById(pBaseId);
            if (pBaseIfo != null) {
                //设置加工基地文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(pBaseId, CommonConstants.FORM_NAME_4);
                baseInfoBiz.setFiles(pBaseIfo, fileInfos);
            }

            //收购
            Integer buyId = processBatch.getBuyId();
            buyInfo = buyInfoBiz.selectById(buyId);
            if (buyInfo != null) {
                //设置收购文件
                List<FileInfo> buyFiles = fileInfoService.getByFormIdAndFormName(buyId,
                        CommonConstants.FORM_NAME_23);
                buyInfoBiz.setFiles(buyInfo, buyFiles);

                harvest = harvestBiz.getOneByTraceCode(buyInfo.getTraceCode());

            } else {
                //收获
                harvest = harvestBiz.selectById(processBatch.getHarvestId());
            }

        } else {
            //收获
            harvest = harvestBiz.getOneByTraceCode(productBatchNumber);
        }
        if (harvest != null) {
            //设置收获文件
            harvest.setHarvestPictureList(fileInfoService.getByFormIdAndFormName(harvest.getId(),
                    CommonConstants.FORM_NAME_12));

            Integer productId = harvest.getProductId();

            //产品
            product = productBiz.selectById(productId);

            //基本作业
            baseWorks = baseWorkBiz.getByProductId(productId);
            if (baseWorks != null) {
                List<Integer> baseWorkIds = baseWorks.stream().map(BaseWork::getId).collect(Collectors.toList());
                //设置基本作业文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(baseWorkIds,
                        CommonConstants.FORM_NAME_5);
                FileInfoUtil.setListField("setWorkPictureList", fileInfos, baseWorks,
                        CommonConstants.FILE_TYPE_1);
            }

            //传感器
            List<Integer> dsIds = growEnvironmentBiz.getDSIdsByList(growEnvironmentBiz.getByPId(productId));
            deviceSensors = deviceSensorBiz.batchSelectByIds(dsIds);
            int dsLen = MyObjectUtil.iterableCount(deviceSensors);
            if (product != null) {

                //设置产品文件
                List<FileInfo> pfs = fileInfoService.getByFormIdAndFormName(productId,
                        CommonConstants.FORM_NAME_15);
                productBiz.setFiles(product, pfs);


                Date startDate = product.getStartDate();
                Date endDate = product.getEndDate();
                //传感器数据
                if (startDate != null && endDate != null && dsLen > 0) {
                    List<DeviceSensorData> dsdList = deviceSensorDataBiz.getByIdsMayOther(dsIds, startDate, endDate);
                    if (dsdList != null) {
                        for (int i = 0; i < dsLen; i++) {
                            DeviceSensor item = deviceSensors.get(i);
                            Integer dsId = item.getId();
                            List<DeviceSensorData> dsdS = dsdList.stream()
                                    .filter(e -> dsId.equals(e.getSensorId())).collect(Collectors.toList());
                            item.setDsdList(dsdS);
                        }
                    }
                }


                Integer baseId = product.getBaseId();
                //生产基地
                baseInfo = baseInfoBiz.selectById(baseId);
                if (baseInfo != null) {
                    //设置生产基地文件
                    List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(baseId,
                            CommonConstants.FORM_NAME_4);
                    baseInfoBiz.setFiles(baseInfo, fileInfos);
                }
                //产品详情（产品）
                if (processBatch == null) {
                    pDId = product.getProductDetailId();
                }

            }
        }
        productDetail = productDetailBiz.selectById(pDId);
        if (productDetail != null) {
            //设置产品详情文件
            List<FileInfo> pDFiles = fileInfoService.getByFormIdAndFormName(pDId,
                    CommonConstants.FORM_NAME_22);
            productDetailBiz.setFiles(productDetail, pDFiles);

            //物联网token
           /* Integer eId = productDetail.getEnterpriseId();
            Enterprise enterprise = enterpriseBiz.selectById(eId);
            if (enterprise != null) {
                JSONObject params = new JSONObject();
                params.put("username", enterprise.getWlUsername());
                params.put("password", enterprise.getWlPassword());

                JSONObject result = URLUtil.postOrPutJson(
                        "http://47.105.215.208:8005/login",
                        "POST", params, null);
                if (result != null) {
                    wlToken = result.getString("token");

                }

            }*/
        }

        obj.put(CommonConstants.FORM_NAME_15, product);
        obj.put(CommonConstants.FORM_NAME_4 + 1, baseInfo);
        obj.put(CommonConstants.FORM_NAME_4 + 2, pBaseIfo);

        obj.put(CommonConstants.FORM_NAME_5, baseWorks);
        obj.put(CommonConstants.FORM_NAME_12, harvest);
        obj.put(CommonConstants.FORM_NAME_14, processBatch);
        obj.put(CommonConstants.FORM_NAME_10, finishProductTests);
        obj.put(CommonConstants.FORM_NAME_17, sales);
        obj.put(CommonConstants.FORM_NAME_22, productDetail);
        obj.put(CommonConstants.FORM_NAME_23, buyInfo);
        obj.put(CommonConstants.FORM_NAME_24, deviceSensors);
        //添加物联网token
        //  obj.put("wlToken", wlToken);
        return obj;
    }

    /**
     * 通过结束时间筛选数据
     */
    public List<ProcessBatch> getPartListByFilter(List<ProcessBatch> list, long startNum, long endNum) {
        if (list != null) {
            return list.stream()
                    .filter(obj -> {
                        Date time = obj.getEndDate();
                        if (time != null) {
                            long ft = time.getTime();
                            return ft >= startNum && ft <= endNum;
                        }
                        return false;
                    }).collect(Collectors.toList());
        }
        return null;
    }


    /**
     * 获得某一企业(某一基地类型)某一年的统计信息
     */
    public JSONObject statisticsByEId(Integer enterpriseId, String baseType, Date before) {
        if (before == null) {
            return null;
        }
        //通过一年的起始时间获得结束时间
        Date after = MyObjectUtil.getOneYearEndDateByStartDate(before);
        long beforeNum = before.getTime();
        long afterNum = after.getTime();

        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject(true);

        //收购
        Integer sysModule = productBiz.getSysModuleByBaseType(baseType);
        List<BuyInfo> buyInfos = buyInfoBiz.getByEId(enterpriseId, sysModule, before, after);

        //获得所有加工批次
        List<ProcessBatch> processBatches = productBiz.getByEnterpriseId(enterpriseId, baseType);
        List<String> productBatchNumbers = productBiz.getPBNByList(processBatches);

        List<Sale> sales = saleBiz.getByPBNs(productBatchNumbers, before, after);
        List<FinishProductTest> finishProductTests = finishProductTestBiz.getByPBNs(productBatchNumbers, before, after);

        //获得结束时间在某一年内的加工批次
        processBatches = getPartListByFilter(processBatches, beforeNum, afterNum);

        //收购数
        obj.put(CommonConstants.FORM_NAME_23, MyObjectUtil.iterableCount(buyInfos));
        //生产批次
        obj.put(CommonConstants.FORM_NAME_14, MyObjectUtil.iterableCount(processBatches));
        //销售订单
        obj.put(CommonConstants.FORM_NAME_17, MyObjectUtil.iterableCount(sales));
        //检验报告
        obj.put(CommonConstants.FORM_NAME_10, MyObjectUtil.iterableCount(finishProductTests));

        for (int j = 0, len = CommonConstants.MONTH_NAME.length; j < len; j++) {
            //设为true，jsonObject中的key输出时才会和put的顺序一样
            JSONObject objM = new JSONObject(true);
            int pcNum = 0;
            int ddNum = 0;
            objM.put("month", CommonConstants.MONTH_NAME[j]);
            Date m = MyObjectUtil.handleDateMonth(before, j);
            long monthStart = m.getTime();
            long monthEnd = MyObjectUtil.getOneMonthEndDateByStartDate(m).getTime();
            //获得结束时间在某一年某个月内的加工批次
            List<ProcessBatch> processBatchesM = getPartListByFilter(processBatches, monthStart, monthEnd);
            pcNum = MyObjectUtil.iterableCount(processBatchesM);
            //获得某个月的销售信息
            if (sales != null) {
                List<Sale> salesM = sales.stream()
                        .filter(sale -> {
                            Date time = sale.getSendDate();
                            if (time != null) {
                                long ft = time.getTime();
                                return ft >= monthStart && ft <= monthEnd;
                            }
                            return false;
                        }).collect(Collectors.toList());
                ddNum = salesM.size();
            }
            objM.put(CommonConstants.FORM_NAME_14, pcNum);
            objM.put(CommonConstants.FORM_NAME_17, ddNum);
            array.add(objM);
        }
        jsonObject.put("upper", obj);
        jsonObject.put("lower", array);
        return jsonObject;
    }

}
