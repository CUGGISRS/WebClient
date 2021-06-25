package com.github.wxiaoqi.security.zs.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.BetweenTestItemBiz;
import com.github.wxiaoqi.security.zs.sys.biz.FinishProductTestBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ItemInfoBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.entity.FinishProductTest;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.util.TestItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 成品检验
 */
@RestController
@RequestMapping("FinishProductTest")
public class FinishProductTestController extends BaseController<FinishProductTestBiz, FinishProductTest> {
    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;
    @Autowired
    private ItemInfoBiz itemInfoBiz;

    /**
     * @api {post} /FinishProductTest/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条成品检验(带文件)信息
     * @apiParam {MultipartFile[]} testResultPictureFiles 检验结果图片
     * @apiParam {String} productBatchNumber 产品批次号
     * @apiParam {BigDecimal} testAmount 检验数量
     * @apiParam {String} testAmountUnit 检验数量单位
     * @apiParam {Date} testDate 检验时间
     * @apiParam {String} testResult 检验结果
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    public ObjectRestResponse<FinishProductTest> addDataAndFile(FinishProductTest obj,
                                                                MultipartFile[] testResultPictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            throw new Exception("成品检验添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_10,
                CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);
        obj.setTestResultPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /FinishProductTest/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条成品检验(带文件)信息
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
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<FinishProductTest> update(FinishProductTest obj,
                                                        MultipartFile[] testResultPictureFiles,
                                                        Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            FinishProductTest oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("成品检验修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_10
                        , CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_10);
                obj.setTestResultPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /FinishProductTest/{id}  删除一条(带文件)、检验_项目
     * @apiDescription 删除一条成品检验(带文件)信息、检验_项目
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<FinishProductTest> remove(@PathVariable int id) throws Exception {
        FinishProductTest old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("成品检验删除失败");
            }
            //删除检验_项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdAndTType(id, CommonConstants.TEST_TYPE_2);
            betweenTestItemBiz.delByItems(betweenTestItems);
            old.setBetweenTestItems(betweenTestItems);

            //删除多文件
            List<String> delUrls = new ArrayList<>();
            old.setTestResultPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_10, delUrls));
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /FinishProductTest/batchDelete 批量删除(带文件)、检验_项目
     * @apiDescription 通过id集合批量删除成品检验(带文件)、检验_项目
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<FinishProductTest> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                //删除检验_项目
                List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdsAndTType(ids, CommonConstants.TEST_TYPE_2);
                betweenTestItemBiz.delByItems(betweenTestItems);

                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("成品检验批量删除失败");
                }

                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_10, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /FinishProductTest/{id} 查询一条（带文件）、检验_项目、项目
     * @apiDescription 查询一条成品检验（带文件）、检验_项目、项目
     * @apiParam {Integer} id 编号
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<FinishProductTest> get(@PathVariable int id) {
        FinishProductTest obj = baseBiz.selectById(id);
        if (obj != null) {
            //设置检验_项目、项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdAndTType(id, CommonConstants.TEST_TYPE_2);
            obj.setBetweenTestItems(betweenTestItems);
            List<ItemInfo> itemInfos = itemInfoBiz.batchSelectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            obj.setItemInfos(itemInfos);

            obj.setTestResultPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_10));
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /FinishProductTest/pageByEId 分页展示某一企业(某一基地类型)下产品批次或某一产品批次的成品检验（检验时间倒序）（带文件）
     * @apiDescription 分页展示某一企业(某一基地类型)下产品批次或某一产品批次的成品检验（检验时间倒序）（带文件）
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
     * @apiGroup 成品检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<FinishProductTest> pageByEId(@RequestParam Map<String, Object> params,
                                                            Date testDateS, Date testDateE) throws Exception {
        String orderBy = "test_date desc";
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        Object productBatchNumber = params.get("productBatchNumber");
        if (MyObjectUtil.noNullOrEmpty(productBatchNumber)) {
            toMap.put("productBatchNumber", productBatchNumber);
        } else {
            List<String> productBatchNumbers = productBiz.getPBNByEnterpriseId(params.get("enterpriseId"),
                    params.get("baseType"));
            if (productBatchNumbers != null) {
                inMap.put("productBatchNumber", productBatchNumbers);
            } else {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }

        toMap.put("testResult", params.get("testResult"));
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("testDate", testDateE);
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("testDate", testDateS);
        TableResultResponse<FinishProductTest> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<FinishProductTest> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(FinishProductTest::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_10);
            FileInfoUtil.setListField("setTestResultPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            //设置检验_项目、项目
          /*  List<BetweenTestItem> betweenTestItems=betweenTestItemBiz.getByTIdsAndTType(ids,CommonConstants.TEST_TYPE_2);
            List<ItemInfo> itemInfos=itemInfoBiz.batchSelectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            TestItemUtil.setListTestItem("setBetweenTestItems",betweenTestItems,
                    "setItemInfos",itemInfos,list,CommonConstants.TEST_TYPE_2);*/
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
