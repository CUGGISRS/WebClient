package com.github.wxiaoqi.security.jd.sys.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.jd.sys.entity.DeptTest;
import com.github.wxiaoqi.security.jd.sys.entity.WarningInfo;
import com.github.wxiaoqi.security.jd.sys.entity.WarningThreshold;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IProductService;
import com.github.wxiaoqi.security.jd.sys.mapper.DeptTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门抽检
 */
@Service
public class DeptTestBiz extends BaseBiz<DeptTestMapper, DeptTest> {
    @Autowired
    private IProductService productService;
    @Autowired
    private WarningInfoBiz warningInfoBiz;

    @Autowired
    private WarningThresholdBiz warningThresholdBiz;

    /**
     * 通过产品批次号查询数据
     */
    public List<DeptTest> getByPBN(String pbn) {
        if (StringHelper.isNullOrEmpty(pbn)) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("productBatchNumber", pbn);
        return getByToMap(toMap);
    }


    /**
     * 通过产品批次号集合查询数据
     */
    public List<DeptTest> getByPBNs(List<String> pbnS) {
        String inFiled = "productBatchNumber";
        return getByInFiledMayToMap(pbnS, inFiled, null);
    }

    /**
     * 通过企业id获得信息
     */
    public List<DeptTest> getByEId(Integer eId){
        if(eId==null){
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("enterpriseId", eId);
        return getByToMap(toMap);
    }


    /**
     * 查询（检验时间大于等于before，小于等于after）的数据
     */
    public List<DeptTest> getByTestDate(Date before, Date after) {
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("testDate", before);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("testDate", after);
        return getMayCondition(null, null, null, null, null, null,
                null, null, null, gtMap, null, ltMap, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }

    /**
     * 提取企业id集合
     */
    public List<Integer> getEIDsByList(List<DeptTest> list) {
        if (list != null) {
            List<Integer> item = list.stream()
                    .map(DeptTest::getEnterpriseId)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(item) > 0) {
                return item;
            }
        }
        return null;
    }

    /**
     * 提取产品批次号集合
     */
    public List<String> getPBNsByList(List<DeptTest> list) {
        if (list != null) {
            List<String> item = list.stream()
                    .map(DeptTest::getProductBatchNumber)
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(item) > 0) {
                return item;
            }
        }
        return null;
    }


    /**
     * 提取产品详情id集合
     */
    public List<Integer> getPDIDsByList(List<DeptTest> list) {
        if (list != null) {
            List<Integer> item = list.stream()
                    .map(DeptTest::getProductDetailId)
                    .distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(item) > 0) {
                return item;
            }
        }
        return null;
    }

    /**
     * 通过产品详情id获得水产或种植总合格率，确定是否添加预警信息
     */
    public void isAddWarning(Integer pdId) throws Exception {
        //获得所属系统模块
        Integer sysModule = productService.getSysModuleByPDId(pdId);
        isAddWarningModule(sysModule);
    }

    /**
     * 通过产品详情id集合获得水产或种植总合格率，确定是否添加预警信息
     */
    public void isAddWarnings(List<Integer> pdIds) throws Exception {

        Map<String, List<Integer>> map = productService.productDetailModule(pdIds);
        if (map == null) {
            return;
        }
        List<Integer> scPDIds = map.get(CommonConstants.SYS_MODULE_SC_KEY);
        List<Integer> zzPDIds = map.get(CommonConstants.SYS_MODULE_ZZ_KEY);
        if (scPDIds.size() > 0) {
            isAddWarningModule(CommonConstants.SYS_MODULE_SC);
        }
        if (zzPDIds.size() > 0) {
            isAddWarningModule(CommonConstants.SYS_MODULE_ZZ);
        }
    }

    /**
     * 判断所有抽检公司的所有产品是否合格，不合格则添加预警信息
     */
    public void isAddWarning() throws Exception {
        //获得所有抽检信息
        List<DeptTest> list = getByTestDate(null, null);
        isAddWarning(list);
    }

    /**
     * 判断某些抽检信息所有抽检公司的所有产品是否合格，不合格则添加预警信息
     */
    public void isAddWarning(List<DeptTest> list) throws Exception {

        int len = MyObjectUtil.iterableCount(list);
        if (len == 0) {
            return;
        }

        //获得阈值(所有公司产品此时共用一个阈值)
        WarningThreshold wt = warningThresholdBiz.selectById(1);
        BigDecimal wtV = wt == null ? null : wt.getThreshold();
        if (wtV == null) {
            throw new Exception("预警阈值不存在");
        }

        List<String> pbnS = getPBNsByList(list);
        //获得加工产品的的预警相关信息
        JSONArray arr = productService.pbnPartMap(pbnS);
        int arrLen = arr.size();

        //根据追溯码分组
        Map<String, List<DeptTest>> map = list.stream()
                .filter(e -> e != null && e.getProductBatchNumber() != null)
                .collect(Collectors.groupingBy(DeptTest::getProductBatchNumber));

        //需要添加的预警信息
        List<WarningInfo> warningInfos = new ArrayList<>();
        for (Map.Entry<String, List<DeptTest>> entry : map.entrySet()) {
            //每个产品对应的追溯码信息
            String pbnKey = entry.getKey();
            List<DeptTest> dtList = entry.getValue();
            if (pbnKey == null || dtList == null) {
                continue;
            }

            //按检验时间正序
            dtList = dtList.stream()
                    .sorted(Comparator.comparing(DeptTest::getTestDate)).collect(Collectors.toList());
            Date lastTestDate = null;
            int num = 0;
            int pNum = 0;
            double pRoute;
            for (int i = 0, vLen = dtList.size(); i < vLen; i++) {
                DeptTest item = dtList.get(i);
                Date testDate = item.getTestDate();
                String result = item.getTestResult();
                num++;
                if (CommonConstants.TEST_RESULT_1.equals(result)) {
                    pNum++;
                }
                if (testDate == null) {
                    continue;
                }
                lastTestDate = testDate;
            }

            //计算某一产品的合格率
            if (num == 0) {
                //无抽检信息时不需要预警
                continue;
            }
            pRoute = (double) pNum * 100 / num;
            //直接转化double会丢失精度
            //合格率低于阈值则添加一条报警信息
            if (wtV.compareTo(new BigDecimal(String.valueOf(pRoute))) > 0) {
                long pRouteZ = Math.round(pRoute);

                for (int i = 0; i < arrLen; i++) {
                    JSONObject item = arr.getJSONObject(i);
                    String pbn = item.getString("pbn");
                    if (pbnKey.equals(pbn)) {
                        String eName = item.getString("eName");
                        String pdName = item.getString("pdName");
                        String start = item.getString("pbStartDate");
                        String end = item.getString("pbEndDate");
                        Integer sysModule = item.getInteger("sysModule");
                        String content = StringHelper.dateYMd2String(lastTestDate) + ",【" + eName + "】" + pdName;
                        if (start != null && end != null) {
                            if (start.equals(end)) {
                                content += "(" + start + ")";
                            } else {
                                content += "(" + start + "至" + end + ")";
                            }
                        }
                        content += "抽检合格率为" + pRouteZ + "%,低于阈值";
                        WarningInfo wi = new WarningInfo(new Date(), sysModule, 0, content);
                        warningInfos.add(wi);
                        break;
                    }
                }

            }

        }
        int index = warningInfoBiz.batchInsertByList(warningInfos);
        if (index != warningInfos.size()) {
            throw new Exception("预警信息添加失败");
        }
    }

    /**
     *获得某一企业的所有产品批次号的合格率
     */
    public JSONArray getPBNPassRateByEId(Integer eId){
        List<DeptTest> list=getByEId(eId);
        int len=MyObjectUtil.iterableCount(list);
        if(len==0){
            return null;
        }
        List<String> pbnS = getPBNsByList(list);
        //获得加工产品的的预警相关信息
        JSONArray arr = productService.pbnPartMap(pbnS);
        int arrLen = arr.size();

        //存储产品批次号的合格率信息
        JSONArray array=new JSONArray();

        //根据追溯码分组
        Map<String, List<DeptTest>> map = list.stream()
                .filter(e -> e != null && e.getProductBatchNumber() != null)
                .collect(Collectors.groupingBy(DeptTest::getProductBatchNumber));

        for (Map.Entry<String, List<DeptTest>> entry : map.entrySet()) {
            //每个产品对应的追溯码信息
            String pbnKey = entry.getKey();
            List<DeptTest> dtList = entry.getValue();
            if (pbnKey == null || dtList == null) {
                continue;
            }

            int num = 0;
            int pNum = 0;
            double pRoute;
            //按检验时间正序
            dtList = dtList.stream()
                    .sorted(Comparator.comparing(DeptTest::getTestDate)).collect(Collectors.toList());
            Date lastTestDate = null;

            for (int i = 0, vLen = dtList.size(); i < vLen; i++) {
                DeptTest item = dtList.get(i);
                num++;
                Date testDate = item.getTestDate();
                String result = item.getTestResult();
                if (CommonConstants.TEST_RESULT_1.equals(result)) {
                    pNum++;
                }
                if (testDate == null) {
                    continue;
                }
                lastTestDate = testDate;
            }

            //计算某一产品的合格率
            if (num == 0) {
                //无抽检信息时不需要预警
                continue;
            }

            pRoute = (double) pNum * 100 / num;
            long pRouteZ = Math.round(pRoute);

            for (int i = 0; i < arrLen; i++) {
                JSONObject item = arr.getJSONObject(i);
                String pbn = item.getString("pbn");
                if (pbnKey.equals(pbn)) {
                    String pdName = item.getString("pdName");
                    String start = item.getString("pbStartDate");
                    String end = item.getString("pbEndDate");

                    String pName =  pdName;
                    if (start != null && end != null) {
                        if (start.equals(end)) {
                            pName += "(" + start + ")";
                        } else {
                            pName += "(" + start + "至" + end + ")";
                        }
                    }

                    JSONObject obj=new JSONObject(true);

                    obj.put("pName",pName);
                    obj.put("lastTestDate",StringHelper.dateYMd2String(lastTestDate));//最后抽检时间
                    obj.put("passRate",pRouteZ);//合格率

                    array.add(obj);
                    break;
                }
            }

        }
        return array;

    }

    /**
     * 获得水产或种植总合格率，确定是否添加预警信息                                                                                                                                                                                                                                                                      判断水产或种植总合格率，确定是否添加预警信息
     */
    public void isAddWarningModule(Integer sysModule) throws Exception {
        String key;
        if (CommonConstants.SYS_MODULE_SC.equals(sysModule)) {
            key = CommonConstants.SYS_MODULE_SC_KEY;
        } else if (CommonConstants.SYS_MODULE_ZZ.equals(sysModule)) {
            key = CommonConstants.SYS_MODULE_ZZ_KEY;
        } else {
            throw new Exception("抽检商品必须属于水产或种植");
        }

        //获得阈值(此时水产、种植共用水产阈值)
        WarningThreshold wt = warningThresholdBiz.selectById(CommonConstants.SYS_MODULE_SC);
        BigDecimal wtV = null;
        if (wt != null) {
            wtV = wt.getThreshold();
        }
        if (wtV == null) {
            throw new Exception("预警阈值不存在");
        }

        List<DeptTest> list = getByTestDate(null, null);
        List<Integer> pdIds = getPDIDsByList(list);
        if (MyObjectUtil.iterableCount(pdIds) > 0) {
            int num = 0;
            int pNum = 0;
            double pRoute;
            Map<String, List<Integer>> map = productService.productDetailModule(pdIds);
            List<Integer> kpdIds = map.get(key);
            List<DeptTest> kList = list.stream().filter(deptTest -> kpdIds.contains(deptTest.getProductDetailId()))
                    .collect(Collectors.toList());

            //按检验时间正序
            kList = kList.stream().sorted(Comparator.comparing(DeptTest::getTestDate)).collect(Collectors.toList());
            Date lastTestDate = null;
            for (int i = 0, len = kList.size(); i < len; i++) {
                DeptTest item = kList.get(i);
                Date testDate = item.getTestDate();
                String result = item.getTestResult();
                if (testDate == null) {
                    continue;
                }
                lastTestDate = testDate;
                num++;
                if (CommonConstants.TEST_RESULT_1.equals(result)) {
                    pNum++;
                }

            }

            //计算所有数据的总合格率
            if (num == 0) {
                //无抽检信息时不需要预警
            } else {
                pRoute = (double) pNum * 100 / num;
                //直接转化double会丢失精度
                //总合格率低于阈值则添加一条报警信息
                if (wtV.compareTo(new BigDecimal(String.valueOf(pRoute))) > 0) {
                    String content = StringHelper.dateYMd2String(lastTestDate);
                    if (CommonConstants.SYS_MODULE_SC.equals(sysModule)) {
                        content += "水产";
                    } else if (CommonConstants.SYS_MODULE_ZZ.equals(sysModule)) {
                        content += "种植";
                    }
                    content += "产品抽检合格率低于阈值，请尽快处理";
                    int index = warningInfoBiz.insertSelective(
                            new WarningInfo(new Date(), sysModule, content));
                    if (index == 0) {
                        throw new Exception("预警信息添加失败");
                    }
                }
            }

        }

    }


    /**
     * 统计各(某一检验时间段)水产、种植总合格率
     */
    public JSONObject statisticsTotalPassRate(Date testDateS, Date testDateE) {
        List<DeptTest> list = getByTestDate(testDateS, testDateE);
        List<Integer> pdIds = getPDIDsByList(list);
        if (pdIds == null) {
            pdIds = new ArrayList<>();
        }
        JSONObject obj = new JSONObject();
        JSONArray scArr = new JSONArray();
        JSONArray zzArr = new JSONArray();
        Map<String, List<Integer>> map = productService.productDetailModule(pdIds);
        List<Integer> scPDIds = map.get(CommonConstants.SYS_MODULE_SC_KEY);
        List<Integer> zzPDIds = map.get(CommonConstants.SYS_MODULE_ZZ_KEY);
        List<DeptTest> scList = new ArrayList<>();//水产
        List<DeptTest> zzList = new ArrayList<>();//种植
        if (list != null) {
            //按检验时间正序
            list = list.stream().sorted(Comparator.comparing(DeptTest::getTestDate)).collect(Collectors.toList());
            for (int i = 0, len = list.size(); i < len; i++) {
                DeptTest item = list.get(i);
                Integer pdId = item.getProductDetailId();
                if (scPDIds.contains(pdId)) {
                    scList.add(item);
                } else if (zzPDIds.contains(pdId)) {
                    zzList.add(item);
                }
            }
            //水产
            int scNum = 0;
            int scPNum = 0;
            double scPRoute;
            for (int i = 0, len = scList.size(); i < len; i++) {
                DeptTest item = scList.get(i);
                Date testDate = item.getTestDate();
                if (testDate == null) {
                    continue;
                }
                scNum++;
                String result = item.getTestResult();
                if (CommonConstants.TEST_RESULT_1.equals(result)) {
                    scPNum++;
                }
                //计算某一时间的总合格率
                if (scNum == 0) {
                    scPRoute = 0;
                } else {
                    scPRoute = (double) scPNum * 100 / scNum;
                }
                JSONArray ar = new JSONArray();
                ar.add(StringHelper.dateYMd2String(testDate));
                ar.add(MyObjectUtil.getOneDecimal(scPRoute));
                scArr.add(ar);
            }
            //种植
            int zzNum = 0;
            int zzPNum = 0;
            double zzPRoute;
            for (int i = 0, len = zzList.size(); i < len; i++) {
                DeptTest item = zzList.get(i);
                Date testDate = item.getTestDate();
                if (testDate == null) {
                    continue;
                }
                zzNum++;
                String result = item.getTestResult();
                if (CommonConstants.TEST_RESULT_1.equals(result)) {
                    zzPNum++;
                }
                //计算某一时间的总合格率
                if (zzNum == 0) {
                    zzPRoute = 0;
                } else {
                    zzPRoute = (double) zzPNum * 100 / zzNum;
                }

                JSONArray ar = new JSONArray();
                ar.add(StringHelper.dateYMd2String(testDate));
                ar.add(MyObjectUtil.getOneDecimal(zzPRoute));
                zzArr.add(ar);
            }

        }
        obj.put(CommonConstants.SYS_MODULE_SC_KEY, duplicateTestDate(scArr));
        obj.put(CommonConstants.SYS_MODULE_ZZ_KEY, duplicateTestDate(zzArr));
        return obj;
    }

    /**
     * 保留最后的合格率进行去重
     */
    private JSONArray duplicateTestDate(JSONArray arr) {
        JSONArray newArr = new JSONArray();
        Map<String, Integer> map = new HashMap<>();
        int len = arr.size();
        for (int i = 0; i < len; i++) {
            JSONArray jsonArray = (JSONArray) arr.get(i);
            String testDate = (String) jsonArray.get(0);
            map.put(testDate, i);
        }
        for (int i = 0; i < len; i++) {
            JSONArray obj = (JSONArray) arr.get(i);
            String testDate = (String) obj.get(0);
            Integer index = map.get(testDate);
            if (index.equals(i)) {
                newArr.add(obj);
            }
        }
        return newArr;
    }


    /**
     * 统计(某一检验时间段)各区域的合格率、检验数量
     */
    public JSONArray statisticsArea(Date testDateS, Date testDateE) {
        List<DeptTest> list = getByTestDate(testDateS, testDateE);
        List<Integer> eIds = getEIDsByList(list);
        if (eIds == null) {
            eIds = new ArrayList<>();
        }
        JSONArray array = new JSONArray();
        Map<String, List<Integer>> map = productService.enterpriseArea(eIds);
        if (map != null) {
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<Integer> value = entry.getValue();
                int num = 0;
                int cNum = 0;
                double cRoute = 0;

                JSONObject obj = new JSONObject();

                if (value != null) {
                    for (int i = 0, len = list.size(); i < len; i++) {
                        DeptTest deptTest = list.get(i);
                        Integer eId = deptTest.getEnterpriseId();
                        String result = deptTest.getTestResult();
                        if (value.contains(eId)) {
                            num++;
                            if (CommonConstants.TEST_RESULT_1.equals(result)) {
                                cNum++;
                            }
                        }
                    }
                    if (num == 0) {
                        cRoute = 0;
                    } else {
                        cRoute = (double) cNum / num;
                    }
                }
                obj.put("name", key);
                obj.put("cNum", num);
                obj.put("cRoute", MyObjectUtil.getThreeDecimal(cRoute));
                array.add(obj);
            }
            return array;
        }
        //此时查不到区域
        return null;
    }


}
