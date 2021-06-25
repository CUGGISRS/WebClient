package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionaryInfo;
import com.github.wxiaoqi.security.dzsw.sys.entity.PriceQuotationInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.PriceQuotationInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TsaiJun
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PriceQuotationInfoBiz extends BaseBiz<PriceQuotationInfoMapper, PriceQuotationInfo> {

    @Resource
    private PriceQuotationInfoMapper priceQuotationInfoMapper;

    @Resource
    private DataDictionaryInfoBiz dataDictionaryInfoBiz;

    /**
     *获得 某一报价时间段的数据
     */
    public List<PriceQuotationInfo> getBetweenOfferTime(Date offerTimeS, Date offerTimeE){
        Map<String ,Object> gtMap=new HashMap<>();
        Map<String ,Object> ltMap=new HashMap<>();
        gtMap.put("offerTime",offerTimeS);
        ltMap.put("offerTime",offerTimeE);
        return getMayCondition(null,null,null,null,null,null,
                null,null,null,gtMap,null,ltMap,null,
                null,null,null,null,null,
                null,null,null,null,null,null,
                null,null);

    }

    /**
     * 统计某一报价时间段的各乡镇各农产品类型的产品种类数量
     */
    public ObjectRestResponse statisticsProductNum(Date offerTimeS, Date offerTimeE){
        JSONArray array=new JSONArray();
        List<PriceQuotationInfo> list=getBetweenOfferTime(offerTimeS,offerTimeE);
       if(list!=null){
           //地区分组
           Map<String,List<PriceQuotationInfo>> map=list.stream()
                   .filter(e->e.getDistrict()!=null)
                   .collect(Collectors.groupingBy(e->e.getDistrict()));
           for (Map.Entry<String, List<PriceQuotationInfo>> entry : map.entrySet()) {
               //地区
               String key=entry.getKey();
               List<PriceQuotationInfo> value = entry.getValue();

               JSONObject obj=new JSONObject(true);
               obj.put("district",key);
               JSONArray arr=new JSONArray();
               //产品类型分组
               Map<String,List<PriceQuotationInfo>> map1=value.stream()
                       .filter(e->e.getProductCategory()!=null)
                       .collect(Collectors.groupingBy(e->e.getProductCategory()));
               for (Map.Entry<String, List<PriceQuotationInfo>> entry1 : map1.entrySet()) {
                   //产品种类名称
                   String key1=entry1.getKey();
                   List<PriceQuotationInfo> value1=entry1.getValue();

                   //利用set获得非重复产品名称个数
                  Set<String> set=value1.stream().map(PriceQuotationInfo::getProductName)
                          .filter(Objects::nonNull)
                          .collect(Collectors.toSet());

                   JSONObject obj1=new JSONObject(true);
                   obj1.put("productCategory",key1);
                   obj1.put("productCategoryNum",set.size());
                   arr.add(obj1);

               }
               obj.put("productNum",arr);
               array.add(obj);

           }
       }
        return new ObjectRestResponse(StatusCode.SUCCESS,true,array);
    }

    /**
     * 分页获取价格行情
     *
     * @return
     */
    public ObjectRestResponse listByPage(Map<String, Object> map) {
        try {
            Query query = new Query(map);
            Example example = getExampleByQuery(query);
            Example.Criteria criteria = example.createCriteria();
            if (!map.get("startTime").equals("") && !map.get("endTime").equals("")) {
                criteria.andBetween("offerTime", map.get("startTime"), map.get("endTime"));
            }
            criteria.andEqualTo("isDeleted", 0);
            example.setOrderByClause("offer_time DESC");
            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<PriceQuotationInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

    /**
     * 显示 指定时间段内 某一类型的农产品价格行情
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse listByTypeAndTime(JSONObject jsonObject) {
        try {
            CommonUtil.fillPageParam(jsonObject);
            if (jsonObject.containsKey("time")) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                // 将当前时间加一天
                calendar.add(Calendar.DATE, 1);
                String endTime = df.format(calendar.getTime());
                Calendar c = Calendar.getInstance();
                if ("近一周".equals(jsonObject.getString("time"))) {
                    //当前时间减6天
                    c.add(Calendar.DATE, -6);
                } else if ("近半个月".equals(jsonObject.getString("time"))) {
                    //当前时间减14天
                    c.add(Calendar.DATE, -14);
                } else if ("近一个月".equals(jsonObject.getString("time"))) {
                    //当前时间减29天
                    c.add(Calendar.DATE, -29);
                } else if ("近半年".equals(jsonObject.getString("time"))) {
                    //当前时间减179天
                    c.add(Calendar.DATE, -179);
                } else if ("近一年".equals(jsonObject.getString("time"))) {
                    //当前时间减364天
                    c.add(Calendar.DATE, -364);
                }
                Date startDate = c.getTime();
                String startTime = df.format(startDate);
                jsonObject.put("startTime", startTime);
                jsonObject.put("endTime", endTime);
            }
            Example example = new Example(PriceQuotationInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("offerTime", jsonObject.get("startTime"), jsonObject.get("endTime"));
            if (jsonObject.containsKey("category")) {
                criteria.andEqualTo("productCategory", jsonObject.get("category"));
            }
            criteria.andEqualTo("isDeleted", 0);
            example.setOrderByClause("offer_time DESC");
            Page<Object> result = PageHelper.startPage(jsonObject.getIntValue("pageNum"), jsonObject.getIntValue("pageRow"));
            List<PriceQuotationInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }
    }

    /**
     * 按报价时间最近随机显示指定个数的价格行情
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse listRecentPriceInfo(JSONObject jsonObject) {
        try {
            Example example = new Example(PriceQuotationInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isDeleted", 0);
            example.setOrderByClause("offer_time DESC");
            Page<Object> result = PageHelper.startPage(1, jsonObject.getIntValue("limit"));
            List<PriceQuotationInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, jsonObject.getIntValue("limit")));
        } catch (Exception e) {
            return new ObjectRestResponse(StatusCode.FAIL, e);
        }
    }

    /**
     * 价格走势分析，根据所选农产品分类，查询某一农产品在所选年度内的价格走势。
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse listByProductAndTime(JSONObject jsonObject) {
        String category = jsonObject.getString("category");
        String product = jsonObject.getString("product");
        int year = jsonObject.getIntValue("year");
        Example example = new Example(PriceQuotationInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productCategory", category);
        criteria.andEqualTo("productName", product);
        Calendar c = Calendar.getInstance();
        c.set(year, 1, 1, 0, 0, 0);
        Date st = c.getTime();
        c.set(year + 1, 1, 1, 0, 0, 0);
        Date ed = c.getTime();
        criteria.andBetween("offerTime", st, ed);
        example.setOrderByClause("offer_time ASC");
        List<PriceQuotationInfo> list = selectByExample(example);
//        List<PriceQuotationInfo> result;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//        for (PriceQuotationInfo priceQuotationInfo : list) {
//            Date y = priceQuotationInfo.getOfferTime();
//            String yStr = sdf.format(y);
//            System.out.println(yStr);
//        }
        return new ObjectRestResponse(StatusCode.SUCCESS, CommonUtil.successPage(list, list.size()));
    }

    // 前端获取可以选择的时间
    public ObjectRestResponse<Map<String, Object>> getAvailableDate(int[] ints) {
        try {
            List<JSONObject> list = priceQuotationInfoMapper.getAvailableDate(ints[0], ints[1]);
            Integer total = priceQuotationInfoMapper.getAvailableDateTotal();
            Map<String, Object> map = new HashMap<>();
            map.put("rows", list);
            map.put("total", total);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

    public ObjectRestResponse getPriceListByCategory(String category) {
        try {
            Example dicExample = new Example(DataDictionaryInfo.class);
            Example.Criteria dicCriteria = dicExample.createCriteria();
            dicCriteria.andEqualTo("type", category);
            List<DataDictionaryInfo> dicList = dataDictionaryInfoBiz.selectByExample(dicExample);
            List<PriceQuotationInfo> result = new ArrayList<>();
            List<PriceQuotationInfo> tempList;
            for (DataDictionaryInfo dataDictionaryInfo : dicList) {
                Example example = new Example(PriceQuotationInfo.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("productCategory", category);
                criteria.andEqualTo("productName", dataDictionaryInfo.getText());
                example.setOrderByClause("offer_time DESC");
                tempList = selectByExample(example);
                if (tempList.size() > 0) result.add(tempList.get(0));
            }

            return new ObjectRestResponse<>(StatusCode.SUCCESS, result);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    public List<Object> getPriceListByOneDate(String date) {
        List<JSONObject> lists = priceQuotationInfoMapper.getPriceListByOneDate(date);
        //返回结果集
        List<Object> responseList = new ArrayList<>();
        HashSet<String> sets = new HashSet<>();
        for (JSONObject jsonObject : lists) {
            sets.add((String) jsonObject.get("product_category"));
        }
        for (String set : sets) {
            JSONObject typeObj = new JSONObject();
            typeObj.put("type", set);
            ArrayList<Object> productList = new ArrayList<>();
            for (JSONObject jsonObject : lists) {
                Object product_category = jsonObject.get("product_category");
                if (set.equals(product_category)) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", jsonObject.get("product_name"));
                    obj.put("price", jsonObject.get("price"));
                    obj.put("source", jsonObject.get("collection_point"));
                    productList.add(obj);
                }
            }
            typeObj.put("list", productList);
            responseList.add(typeObj);
        }
        return responseList;
    }

    public ObjectRestResponse importByExcel(MultipartFile file) {
        List<PriceQuotationInfo> list = new ArrayList<>();
        try {
            //获得文件名
            String fileName = file.getOriginalFilename();
            InputStream in = file.getInputStream();
            PriceQuotationInfo priceQuotationInfo = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
            Workbook hssfWorkbook = null;
            if (fileName.endsWith("xlsx")) {
                hssfWorkbook = new XSSFWorkbook(in);//Excel 2007
            } else if (fileName.endsWith("xls")) {
                hssfWorkbook = new HSSFWorkbook(in);//Excel 2003
            }
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // 循环行Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        Cell number = hssfRow.getCell(0);
                        if (number == null || number.toString().equals(""))
                            break;
                        priceQuotationInfo = new PriceQuotationInfo();
                        Cell productCategory = hssfRow.getCell(1); // 产品分类
                        Cell productName = hssfRow.getCell(2);  //产品名称
                        Cell price = hssfRow.getCell(3); // 价格
                        Cell highestPrice = hssfRow.getCell(4); // 最高价
                        Cell lowestPrice = hssfRow.getCell(5); // 最低价
                        Cell offerTime = hssfRow.getCell(6); // 报价时间
                        Cell collectionPoint = hssfRow.getCell(7); // 采集点
                        Cell district = hssfRow.getCell(8); // 乡镇
                        Cell pricingOffer = hssfRow.getCell(9); // 报价员
                        priceQuotationInfo.setProductCategory(productCategory.toString());
                        priceQuotationInfo.setProductName(productName.toString());
                        priceQuotationInfo.setPrice(new BigDecimal(price.toString()));
                        priceQuotationInfo.setHighestPrice(new BigDecimal(highestPrice.toString()));
                        priceQuotationInfo.setLowestPrice(new BigDecimal(lowestPrice.toString()));
                        priceQuotationInfo.setOfferTime(offerTime.getDateCellValue());
                        priceQuotationInfo.setCollectionPoint(collectionPoint.toString());
                        priceQuotationInfo.setDistrict(district.toString());
                        priceQuotationInfo.setPricingOffer(pricingOffer.toString());
                        priceQuotationInfo.setIsDeleted(0);
                        list.add(priceQuotationInfo);
                    }
                }
            }
            int affect = priceQuotationInfoMapper.insertList(list);
            if (affect > 0) {
                return new ObjectRestResponse(StatusCode.SUCCESS);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectRestResponse(StatusCode.FAIL, e);
        }
    }

}
