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
import com.github.wxiaoqi.security.zs.sys.biz.ItemInfoBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.biz.RegularTestBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import com.github.wxiaoqi.security.zs.sys.entity.RegularTest;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.util.TestItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 定期检验(无系统使用)
 */
@RestController
@RequestMapping("RegularTest")
public class RegularTestController extends BaseController<RegularTestBiz, RegularTest> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProductBiz productBiz;

    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;
    @Autowired
    private ItemInfoBiz itemInfoBiz;

    /**
     * @api {post} /RegularTest/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条定期检验(带文件)信息
     * @apiParam {MultipartFile[]} testResultPictureFiles 检验结果图片
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
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
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<RegularTest> addDataAndFile(RegularTest obj,
                                                          MultipartFile[] testResultPictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("定期检验添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_16,
                CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);
        obj.setTestResultPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /RegularTest/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条定期检验(带文件)信息
     * @apiParam {MultipartFile[]} testResultPictureFiles 检验结果图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
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
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<RegularTest> update(RegularTest obj,
                                                  MultipartFile[] testResultPictureFiles,
                                                  Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            RegularTest oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("定期检验修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_16
                        , CommonConstants.FILE_TYPE_1, testResultPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_16);
                obj.setTestResultPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /RegularTest/{id}  删除一条(带文件)、检验_项目
     * @apiDescription 删除一条定期检验(带文件)信息、检验_项目
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<RegularTest> remove(@PathVariable int id) throws Exception {
        RegularTest old = baseBiz.selectById(id);
        if (old != null) {
            //删除检验_项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdAndTType(id, CommonConstants.TEST_TYPE_1);
            betweenTestItemBiz.delByItems(betweenTestItems);
            old.setBetweenTestItems(betweenTestItems);

            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("定期检验删除失败");
            }

            //删除多文件
            List<String> delUrls = new ArrayList<>();
            old.setTestResultPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_16, delUrls));
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /RegularTest/batchDelete 批量删除(带文件)、检验_项目
     * @apiDescription 通过id集合批量删除定期检验(带文件)、检验_项目
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<RegularTest> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("定期检验批量删除失败");
                }
                //删除检验_项目
                List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdsAndTType(ids, CommonConstants.TEST_TYPE_1);
                betweenTestItemBiz.delByItems(betweenTestItems);

                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_16, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /RegularTest/{id} 查询一条（带文件）、检验_项目、项目
     * @apiDescription 查询一条定期检验（带文件）、检验_项目、项目
     * @apiParam {Integer} id 编号
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<RegularTest> get(@PathVariable int id) {
        RegularTest obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setTestResultPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_16));
            //设置检验_项目、项目
            List<BetweenTestItem> betweenTestItems = betweenTestItemBiz.getByTIdAndTType(id, CommonConstants.TEST_TYPE_1);
            obj.setBetweenTestItems(betweenTestItems);
            List<ItemInfo> itemInfos = itemInfoBiz.batchSelectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            obj.setItemInfos(itemInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /RegularTest/pageByEId 分页展示某一企业(某一基地类型)下产品或某一产品的定期检验（检验时间倒序）（带文件）
     * @apiDescription 分页展示某一企业(某一基地类型)下产品或某一产品的定期检验（检验时间倒序）（带文件）
     * @apiParam {Integer} enterpriseId 企业id（=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} testDateS 检验时间起点(>=)
     * @apiParam {Date} testDateE 检验时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} productId 产品id(=)
     * @apiParam {String} productName 产品名称
     * @apiParam {BigDecimal} testAmount 检验数量
     * @apiParam {String} testAmountUnit 检验数量单位
     * @apiParam {Date} testDate 检验时间
     * @apiParam {String} testResult 检验结果
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiGroup 定期检验
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<RegularTest> pageByEId(@RequestParam Map<String, Object> params,
                                                      Date testDateS, Date testDateE) throws Exception {
        String orderBy = "test_date desc";
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        Object productId = params.get("productId");
        if (MyObjectUtil.noNullOrEmpty(productId)) {
            toMap.put("productId", productId);
        } else {
            List<Integer> productIds = productBiz.getByEIdAndBaseType(params.get("enterpriseId"),
                    params.get("baseType"));
            if (productIds != null) {
                inMap.put("productId", productIds);
            } else {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }

        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("testDate", testDateS);
        toMap.put("testResult", params.get("testResult"));
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("testDate", testDateE);
        TableResultResponse<RegularTest> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<RegularTest> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(RegularTest::getId).collect(Collectors.toList());
            //设置检验_项目、项目
          /*  List<BetweenTestItem> betweenTestItems=betweenTestItemBiz.getByTIdsAndTType(ids,CommonConstants.TEST_TYPE_1);
            List<ItemInfo> itemInfos=itemInfoBiz.batchSelectByIds(TestItemUtil.getItemIdsByItems(betweenTestItems));
            TestItemUtil.setListTestItem("setBetweenTestItems",betweenTestItems,
                    "setItemInfos",itemInfos,list,CommonConstants.TEST_TYPE_1);*/

            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_16);
            FileInfoUtil.setListField("setTestResultPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
