package com.github.wxiaoqi.security.jd.sys.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.biz.DeptTestBiz;
import com.github.wxiaoqi.security.jd.sys.entity.DeptTest;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IBetweenTestItemService;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IFileInfoService;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IItemInfoService;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IProductService;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import com.github.wxiaoqi.security.zs.sys.util.TestItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门抽检
 */
@RestController
@RequestMapping("DeptTest")
public class DeptTestController extends BaseController<DeptTestBiz, DeptTest> {
    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IItemInfoService itemInfoService;
    @Autowired
    private IBetweenTestItemService betweenTestItemService;

    @GetMapping("getIdsByTraceCode")
    public List<Integer> getIdsByTraceCode(String traceCode) {
        List<DeptTest> deptTests = baseBiz.getByPBN(traceCode);
        return MyObjectUtil.getIdsByList(deptTests);
    }

    @PostMapping("getIdsByTraceCodes")
    public List<Integer> getIdsByTraceCodes(@RequestBody List<String> traceCodes) {
        List<DeptTest> deptTests = baseBiz.getByPBNs(traceCodes);
        return MyObjectUtil.getIdsByList(deptTests);
    }

    @PostMapping("delByIds")
    public int delByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchDeleteByIds(ids);
    }

    /**
     * @api {post} /DeptTest/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条部门抽检(带文件)信息
     * @apiParam {MultipartFile[]} testResultPictureFiles 检验结果图片
     * @apiParam {String} productBatchNumber 产品批次号
     * @apiParam {BigDecimal} testAmount 检验数量
     * @apiParam {String} testAmountUnit 检验数量单位
     * @apiParam {Date} testDate 检验时间
     * @apiParam {String} testResult 检验结果
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<DeptTest> addDataAndFile(DeptTest obj,
                                                       MultipartFile[] testResultPictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("部门抽检添加失败");
        }
        //是否需要添加预警信息
        String pbn = obj.getProductBatchNumber();
        List<DeptTest> list = baseBiz.getByPBN(pbn);
        baseBiz.isAddWarning(list);
      /*  Integer pdId=obj.getProductDetailId();
        baseBiz.isAddWarning(pdId);
*/
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_3,
                CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);
        obj.setTestResultPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /DeptTest/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条部门抽检(带文件)信息
     * @apiParam {MultipartFile[]} testResultPictureFiles 检验结果图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} productBatchNumber 产品批次号
     * @apiParam {BigDecimal} testAmount 检验数量
     * @apiParam {String} testAmountUnit 检验数量单位
     * @apiParam {Date} testDate 检验时间
     * @apiParam {String} testResult 检验结果
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @ResponseBody
    public ObjectRestResponse<DeptTest> update(DeptTest obj,
                                               MultipartFile[] testResultPictureFiles,
                                               Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            DeptTest oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("部门抽检修改失败");
                }

                //是否需要添加预警信息
                String pbn1 = obj.getProductBatchNumber();
                String pbn2 = oldData.getProductBatchNumber();
                List<String> pbnS = new ArrayList<>();
                pbnS.add(pbn1);
                pbnS.add(pbn2);
                List<DeptTest> list = baseBiz.getByPBNs(pbnS);
                baseBiz.isAddWarning(list);


                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_3
                        , CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_3);
                obj.setTestResultPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /DeptTest/{id}  删除一条(带文件)、检验_项目
     * @apiDescription 删除一条部门抽检(带文件)信息、检验_项目
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<DeptTest> remove(@PathVariable int id) throws Exception {
        DeptTest old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("部门抽检删除失败");
            }
            //是否需要添加预警信息
         /*   Integer pdId=old.getProductDetailId();
            baseBiz.isAddWarning(pdId);*/
            String pbn = old.getProductBatchNumber();
            List<DeptTest> list = baseBiz.getByPBN(pbn);
            baseBiz.isAddWarning(list);

            //删除检验_项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemService.getByTIdAndTType(id, CommonConstants.TEST_TYPE_3);
            betweenTestItemService.delByItems(betweenTestItems);
            old.setBetweenTestItems(betweenTestItems);

            //删除多文件
            List<String> delUrls = new ArrayList<>();
            old.setTestResultPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_3, delUrls));
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /DeptTest/batchDelete 批量删除(带文件)、检验_项目
     * @apiDescription 通过id集合批量删除部门抽检(带文件)、检验_项目
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<DeptTest> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("部门抽检批量删除失败");
                }
                //是否需要添加预警信息
              /*  List<Integer> pdIds=baseBiz.getPDIDsByList(oldList);
                baseBiz.isAddWarnings(pdIds);*/
                List<String> pbnS = baseBiz.getPBNsByList(oldList);
                List<DeptTest> dtS = baseBiz.getByPBNs(pbnS);
                baseBiz.isAddWarning(dtS);
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_3, delUrls);
                fileInfoService.delFileByUrls(delUrls);

                //删除检验_项目
                List<BetweenTestItem> betweenTestItems = betweenTestItemService.getByTIdsAndTType(ids, CommonConstants.TEST_TYPE_3);
                betweenTestItemService.delByItems(betweenTestItems);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /DeptTest/{id} 查询一条（带文件）、检验_项目、项目
     * @apiDescription 查询一条部门抽检（带文件）、检验_项目、项目
     * @apiParam {Integer} id 编号
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<DeptTest> get(@PathVariable int id) {
        DeptTest obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setTestResultPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_3));
            //设置检验_项目、项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemService.getByTIdAndTType(id, CommonConstants.TEST_TYPE_3);
            obj.setBetweenTestItems(betweenTestItems);
            List<ItemInfo> itemInfos = itemInfoService.selectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            obj.setItemInfos(itemInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /DeptTest/pageByEId 分页展示(某一企业(某一基地类型)或某一产品批次)的部门抽检（检验时间倒序）（带文件）
     * @apiDescription 分页展示(某一企业 ( 某一基地类型)或某一产品批次)的部门抽检（检验时间倒序）（带文件）
     * @apiParam {Integer} enterpriseId 企业id（=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} testDateS 检验时间起点(>=)
     * @apiParam {Date} testDateE 检验时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} productBatchNumber 产品批次号（=）
     * @apiParam {BigDecimal} testAmount 检验数量
     * @apiParam {String} testAmountUnit 检验数量单位
     * @apiParam {Date} testDate 检验时间
     * @apiParam {String} testResult 检验结果（=）
     * @apiParam {Integer} operatorId 操作人id（关联企业用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<DeptTest> pageByEId(@RequestParam Map<String, Object> params,
                                                   Date testDateS, Date testDateE) throws Exception {
        String orderBy = "test_date desc";
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        Object productBatchNumber = params.get("productBatchNumber");
        if (MyObjectUtil.noNullOrEmpty(productBatchNumber)) {
            toMap.put("productBatchNumber", productBatchNumber);
        } else {
            List<String> productBatchNumbers = productService.getPBNByEnterpriseId(params.get("enterpriseId"),
                    params.get("baseType"));
            if (productBatchNumbers != null) {
                inMap.put("productBatchNumber", productBatchNumbers);
            }
        }

        toMap.put("testResult", params.get("testResult"));
        Map<String, Object> ltMap = new HashMap<>();
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("testDate", testDateS);
        ltMap.put("testDate", testDateE);
        TableResultResponse<DeptTest> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<DeptTest> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(DeptTest::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_3);
            FileInfoUtil.setListField("setTestResultPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);

            //设置检验_项目、项目
           /* List<BetweenTestItem> betweenTestItems=betweenTestItemService.getByTIdsAndTType(ids,CommonConstants.TEST_TYPE_3);
            List<ItemInfo> itemInfos=itemInfoService.selectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            TestItemUtil.setListTestItem("setBetweenTestItems",betweenTestItems,
                    "setItemInfos",itemInfos,list,CommonConstants.TEST_TYPE_3);*/
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * @api {get} /DeptTest/statisticsArea 统计(某一检验时间段)各区域的合格率、检验数量
     * @apiDescription 统计(某一检验时间段)各区域的合格率、检验数量
     * @apiParam {Date} testDateS 检验时间起点(>=)
     * @apiParam {Date} testDateE 检验时间终点(<=)
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("statisticsArea")
    public ObjectRestResponse<JSONArray> statisticsByEId(Date testDateS, Date testDateE) throws Exception {
        JSONArray obj = baseBiz.statisticsArea(testDateS, testDateE);
        if (obj == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {get} /DeptTest/statisticsTotalPassRate 统计各(某一检验时间段)水产、种植总合格率
     * @apiDescription 统计(某一检验时间段)水产、种植总合格率
     * @apiParam {Date} testDateS 检验时间起点(>=)
     * @apiParam {Date} testDateE 检验时间终点(<=)
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("statisticsTotalPassRate")
    public ObjectRestResponse<JSONObject> statisticsTotalPassRate(Date testDateS, Date testDateE) throws Exception {
        JSONObject obj = baseBiz.statisticsTotalPassRate(testDateS, testDateE);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {get} /DeptTest/getPBNPassRateByEId 获得某一企业的所有产品批次号的合格率
     * @apiDescription 无
     * @apiParam {Integer} enterpriseId 企业id
     * @apiGroup 部门抽检
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("getPBNPassRateByEId")
    public ObjectRestResponse<JSONArray> getPBNPassRateByEId(Integer enterpriseId) {
        JSONArray obj = baseBiz.getPBNPassRateByEId(enterpriseId);
        if (obj == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }



}
