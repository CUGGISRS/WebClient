package com.github.wxiaoqi.security.zs.sys.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.feign.service.IDataDictionaryService;
import com.github.wxiaoqi.security.zs.sys.feign.service.IDeptTestService;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品
 */
@Service
public class ProductBiz extends BaseBiz<ProductMapper, Product> {
    @Autowired
    private HarvestBiz harvestBiz;
    @Autowired
    private ProcessBatchBiz processBatchBiz;
    @Autowired
    private BaseInfoBiz baseInfoBiz;
    @Autowired
    private BuyInfoBiz buyInfoBiz;
    @Autowired
    private IDataDictionaryService dataDictionaryService;
    @Autowired
    public EnterpriseBiz enterpriseBiz;

    @Autowired
    private ProductDetailBiz productDetailBiz;

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private BaseWorkBiz baseWorkBiz;
    @Autowired
    protected GrowEnvironmentBiz growEnvironmentBiz;

    @Autowired
    private FinishProductTestBiz finishProductTestBiz;
    @Autowired
    private IDeptTestService deptTestService;

    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;
    @Autowired
    private SaleBiz saleBiz;
    @Autowired
    private RegularTestBiz regularTestBiz;

    /**
     * 修改某一基地的数据
     */
    public int updateByBaseId(Product obj, Integer baseId) {
        if (baseId == null) {
            return 0;
        }
        String fieldName = "baseId";
        return updateByFiled(obj, baseId, fieldName);
    }

    /**
     * 获得这些加工产品追溯码的预警相关信息
     */
    public JSONArray pbnPartMap(List<String> pbnList) {
        //加工抽检
        List<ProcessBatch> processBatches = processBatchBiz.getByPBNs(pbnList);
        int len = MyObjectUtil.iterableCount(processBatches);
        JSONArray pbArr = new JSONArray();

        if (len > 0) {
            List<Integer> pdIds = processBatches.stream()
                    .map(ProcessBatch::getProductDetailId).distinct()
                    .filter(Objects::nonNull).collect(Collectors.toList());

            List<ProductDetail> pdList = productDetailBiz.batchSelectByIds(pdIds);
            int pdLen = MyObjectUtil.iterableCount(pdList);

            //获得所有公司id集合
            List<Integer> eIds = pdLen == 0 ? null : pdList.stream()
                    .map(ProductDetail::getEnterpriseId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<Enterprise> enterprises = enterpriseBiz.batchSelectByIds(eIds);
            int eLen = MyObjectUtil.iterableCount(enterprises);

            for (int i = 0; i < len; i++) {
                ProcessBatch pb = processBatches.get(i);
                String pbn = pb.getProductBatchNumber();
                Integer pdId = pb.getProductDetailId();
                if (pbn == null || pdId == null) {
                    continue;
                }
                String pdName = null;
                Integer sysModule = null;

                String eName = null;
                if (pdLen > 0) {
                    for (int j = 0; j < pdLen; j++) {
                        ProductDetail item = pdList.get(j);
                        Integer id = item.getId();
                        if (id.equals(pdId)) {
                            pdName = item.getName();
                            sysModule = item.getSysModule();
                            Integer eId = item.getEnterpriseId();
                            if (eId != null && eLen > 0) {
                                for (int k = 0; k < eLen; k++) {
                                    Enterprise e = enterprises.get(k);
                                    if (eId.equals(e.getId())) {
                                        eName = e.getName();
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

                JSONObject obj = new JSONObject();
                obj.put("pbn", pbn);//追溯码
                obj.put("pdName", pdName);//品种名称
                obj.put("sysModule", sysModule);//系统模块

                String start = StringHelper.dateYMd2String(pb.getStartDate());
                String end = StringHelper.dateYMd2String(pb.getEndDate());
                obj.put("pbStartDate", start);//加工开始时间
                obj.put("pbEndDate", end);//加工结束时间
                obj.put("eName", eName);//企业名称
                pbArr.add(obj);
            }
        }

        return pbArr;
    }


    /**
     * 将产品详情分水产、种植模块后存入map
     */
    public Map<String, List<Integer>> productDetailModule(List<Integer> pdIds) {
        List<ProductDetail> productDetails = productDetailBiz.batchSelectByIds(pdIds);
        List<Integer> scIds = new ArrayList<>();
        List<Integer> zzIds = new ArrayList<>();
        Map<String, List<Integer>> obj = new HashMap<>();
        if (productDetails != null) {
            for (int i = 0, len = productDetails.size(); i < len; i++) {
                ProductDetail item = productDetails.get(i);
                Integer sysModule = item.getSysModule();
                Integer id = item.getId();
                if (CommonConstants.SYS_MODULE_SC.equals(sysModule)) {
                    scIds.add(id);
                } else if (CommonConstants.SYS_MODULE_ZZ.equals(sysModule)) {
                    zzIds.add(id);
                }
            }
        }
        obj.put(CommonConstants.SYS_MODULE_SC_KEY, scIds);
        obj.put(CommonConstants.SYS_MODULE_ZZ_KEY, zzIds);
        return obj;
    }

    /**
     * 将企业分区域后存入map
     */
    public Map<String, List<Integer>> enterpriseArea(List<Integer> eIds) {
        //获得所有区域
        List<DataDictionary> dataDictionaries = dataDictionaryService.getAllMayToCondition(
                CommonConstants.DATA_TYPE_1, null, null, null);
        int sum = MyObjectUtil.iterableCount(dataDictionaries);
        if (sum > 0) {
            Map<String, List<Integer>> obj = new HashMap<>();
            List<Enterprise> enterprises = enterpriseBiz.batchSelectByIds(eIds);
            for (int i = 0; i < sum; i++) {
                DataDictionary item = dataDictionaries.get(i);
                String code = item.getCode();
                String name = item.getName();
                if (StringHelper.isNullOrEmpty(code) || StringHelper.isNullOrEmpty(name)) {
                    continue;
                }
                //获得属于某一区域的企业
                List<Integer> ps = null;
                if (enterprises != null) {
                    ps = enterprises.stream().filter(enterprise -> code.equals(enterprise.getDistrict()))
                            .map(Enterprise::getId).collect(Collectors.toList());
                }
                obj.put(name, ps);
            }
            return obj;
        }
        return null;
    }

    /**
     * 获得某一企业(某一系统模块)的收购表id集合
     */
    public List<Integer> getBuyIdsByEId(Object enterpriseId, Object sysModule) {
        List<BuyInfo> buyInfos = buyInfoBiz.getByEId(enterpriseId, sysModule, null, null);
        return buyInfoBiz.getIdsByList(buyInfos);
    }

    /**
     * 通过基地id集合获得数据
     */
    public List<Product> getByBaseIds(List<Integer> baseIds) {
        String propertyName = "baseId";
        return getByInFiledMayToMap(baseIds, propertyName, null);
    }


    /**
     * 获得某一企业(某一基地类型)的基地id集合
     */
    public List<Integer> getBaseIdsByEIdAndBaseType(Object enterpriseId, Object baseType) {
        List<BaseInfo> baseInfos = baseInfoBiz.getByEIdAndBaseType(enterpriseId, baseType);
        return baseInfoBiz.getIdsByList(baseInfos);
    }

    /**
     * 获得某一企业(某一基地类型)的产品id集合
     */
    public List<Integer> getByEIdAndBaseType(Object enterpriseId, Object baseType) {
        List<Integer> baseIds = getBaseIdsByEIdAndBaseType(enterpriseId, baseType);
        List<Product> list = getByBaseIds(baseIds);
        return getIdsByList(list);
    }

    /**
     * 获得某一企业(某一基地类型)的收获id集合
     */
    public List<Integer> getHarvestIdsByEIdAndBaseType(Object enterpriseId, Object baseType) {
        List<Harvest> harvests = getHarvestByEIdAndBaseType(enterpriseId, baseType);
        return harvestBiz.getIdsByList(harvests);
    }

    /**
     * 获得某一企业(某一基地类型)的收获集合
     *
     * @param enterpriseId
     * @return
     */
    public List<Harvest> getHarvestByEIdAndBaseType(Object enterpriseId, Object baseType) {
        List<Integer> pIds = getByEIdAndBaseType(enterpriseId, baseType);
        return harvestBiz.getByPIds(pIds);
    }

    /**
     * 获得某一企业(某一基地类型)的加工批次集合(收获、收购)
     *
     * @param enterpriseId
     * @return
     */
    public List<ProcessBatch> getByEnterpriseId(Object enterpriseId, Object baseType) {
        //收获
        List<Integer> hIds = getHarvestIdsByEIdAndBaseType(enterpriseId, baseType);
        List<ProcessBatch> processBatches = processBatchBiz.getByHIds(hIds);
        //收购
        Integer sysModule = getSysModuleByBaseType(baseType);
        List<Integer> bIds = getBuyIdsByEId(enterpriseId, sysModule);
        List<ProcessBatch> bProcessBatches = processBatchBiz.getByBIds(bIds);
        //合并
        if (processBatches != null) {
            if (bProcessBatches != null) {
                processBatches.addAll(bProcessBatches);
            }
        } else {
            processBatches = bProcessBatches;
        }
        return processBatches;
    }


    /**
     * 获得某一企业(某一基地类型)的产品批次号集合(收获、收购)
     *
     * @param enterpriseId
     * @return
     */
    public List<String> getPBNByEnterpriseId(Object enterpriseId, Object baseType) {
        List<ProcessBatch> processBatches = getByEnterpriseId(enterpriseId, baseType);
        return getPBNByList(processBatches);
    }


    /**
     * 设置文件属性值
     */
    public void setFiles(Product obj, List<FileInfo> fileInfos) {
        if (obj != null && fileInfos != null && fileInfos.size() > 0) {
            obj.setPictureList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_1.equals(v.getType())
            ).collect(Collectors.toList()));
        }
    }

    /**
     * 根据基地类型获得系统模块
     */
    public static Integer getSysModuleByBaseType(Object baseType) {
        if (CommonConstants.BASE_TYPE_1.equals(baseType)) {
            return 1;
        } else if (CommonConstants.BASE_TYPE_2.equals(baseType)) {
            return 2;
        }
        return null;
    }


    /**
     * 通过集合获得id集合
     */
    public List<Integer> getIdsByList(List<Product> list) {
        if (list != null) {
            List<Integer> ids = list.stream().map(Product::getId).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(ids) > 0) {
                return ids;
            }
        }
        return null;
    }


    /**
     * 通过id删除产品（带文件）、基本作业(带文件)、生长环境、收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、
     * 成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Product delLinkAndFile(Integer id) throws Exception {
        Product old = selectById(id);
        if (old != null) {
            int index = deleteById(id);
            if (index == 0) {
                throw new Exception("产品删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            List<FileInfo> fileInfos = fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_15, delUrls);
            setFiles(old, fileInfos);

            //基本作业(带文件)
            List<BaseWork> baseWorks = baseWorkBiz.getByProductId(id);
            List<Integer> bwIds = MyObjectUtil.getIdsByList(baseWorks);
            fileInfoService.commonDelByFIdsAndFName(bwIds, CommonConstants.FORM_NAME_5, delUrls);
            baseWorkBiz.batchDeleteByIds(bwIds);

            //生长环境
            growEnvironmentBiz.delByPId(id);


            //收获（带文件）
            List<Harvest> harvests = harvestBiz.getByPId(id);
            delCommon(harvests, delUrls);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);

        }
        return old;
    }

    /**
     * 通过id删除产品（带文件）、基本作业(带文件)、生长环境、收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、
     * 成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public Integer delLinkAndFile(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<Product> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("产品批量删除失败");
            }
            //多文件
            List<String> delUrls = new ArrayList<>();
            fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_15, delUrls);

            //基本作业(带文件)
            List<BaseWork> baseWorks = baseWorkBiz.getByProductIds(ids);
            List<Integer> bwIds = MyObjectUtil.getIdsByList(baseWorks);
            fileInfoService.commonDelByFIdsAndFName(bwIds, CommonConstants.FORM_NAME_5, delUrls);
            baseWorkBiz.batchDeleteByIds(bwIds);

            //生长环境
            growEnvironmentBiz.delByPIds(ids);

            //收获（带文件）
            List<Harvest> harvests = harvestBiz.getByPIds(ids);
            delCommon(harvests, delUrls);

            //一起删除所有文件
            fileInfoService.delFileByUrls(delUrls);
            return sum;
        }
        return null;
    }


    private void delCommon(List<Harvest> harvests, List<String> delUrls) throws Exception {
        List<Integer> hIds = MyObjectUtil.getIdsByList(harvests);
        fileInfoService.commonDelByFIdsAndFName(hIds, CommonConstants.FORM_NAME_12, delUrls);
        harvestBiz.batchDeleteByIds(hIds);

        //追溯码集合
        List<String> traceCodes = harvestBiz.getTraceCodesByList(harvests);

        harvestBiz.delCommon(hIds, traceCodes, delUrls);
    }


    /**
     * 通过对象集合获得产品详情id集合
     */
    public List<Integer> getPDIdsByList(List<Product> list) {
        if (list != null) {
            List<Integer> obj = list.stream()
                    .map(Product::getProductDetailId).distinct()
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (obj != null && obj.size() > 0) {
                return obj;
            }
        }
        return null;
    }

    /**
     * 通过对象集合获得产品批次号集合
     */
    public List<String> getPBNByList(List<ProcessBatch> list) {
        if (list != null) {
            List<String> productBatchNumbers = list.stream()
                    .map(ProcessBatch::getProductBatchNumber)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (productBatchNumbers != null && productBatchNumbers.size() > 0) {
                return productBatchNumbers;
            }
        }
        return null;
    }
}
