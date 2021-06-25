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
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.zs.sys.biz.HarvestBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProcessBatchBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.Harvest;
import com.github.wxiaoqi.security.zs.sys.entity.ProcessBatch;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.util.UniqueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 收获
 */
@RestController
@RequestMapping("Harvest")
public class HarvestController extends BaseController<HarvestBiz, Harvest> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private ProcessBatchBiz processBatchBiz;

    /**
     * @api {post} /Harvest/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条收获(带文件)信息
     * @apiParam {MultipartFile[]} harvestPictureFiles 收获图片
     * @apiParam {String} traceCode 追溯码（唯一，后端生成）
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
     * @apiParam {BigDecimal} harvestAmount 收获数量
     * @apiParam {String} harvestAmountUnit 收获数量单位
     * @apiParam {String} harvestWay 收获方式
     * @apiParam {Date} harvestDate 收获时间
     * @apiParam {String} weather 天气状况
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<Harvest> addDataAndFile(Harvest obj,
                                                      MultipartFile[] harvestPictureFiles) throws Exception {
        //生成20位唯一码
        String code = UniqueUtil.getUniqueCode();
        //是否唯一
        Harvest old = baseBiz.getOneByTraceCode(code);
        ProcessBatch pOld = processBatchBiz.getOneByPBN(code);
        if (old != null || pOld != null) {
            throw new Exception("该追溯码已存在");
        }
        obj.setTraceCode(code);

        List<String> delUrls = new ArrayList<>();
        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("收获添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_12,
                CommonConstants.FILE_TYPE_1, harvestPictureFiles, delUrls);
        obj.setHarvestPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /Harvest/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条收获(带文件)信息
     * @apiParam {MultipartFile[]} harvestPictureFiles 收获图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} traceCode 追溯码（唯一）
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
     * @apiParam {BigDecimal} harvestAmount 收获数量
     * @apiParam {String} harvestAmountUnit 收获数量单位
     * @apiParam {String} harvestWay 收获方式
     * @apiParam {Date} harvestDate 收获时间
     * @apiParam {String} weather 天气状况
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<Harvest> update(Harvest obj,
                                              MultipartFile[] harvestPictureFiles,
                                              Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            Harvest oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //是否唯一
                String code = obj.getTraceCode();
                Harvest old = baseBiz.getOneByTraceCode(code);
                ProcessBatch pOld = processBatchBiz.getOneByPBN(code);
                if (pOld != null || (old != null && !id.equals(old.getId()))) {
                    throw new Exception("该追溯码已存在");
                }

                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("收获修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_12
                        , CommonConstants.FILE_TYPE_1, harvestPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_12);
                obj.setHarvestPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /Harvest/{id}  删除一条收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Harvest> remove(@PathVariable int id) throws Exception {
        Harvest old = baseBiz.delLinkAndFile(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /Harvest/batchDelete 批量删除收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delLinkAndFile(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /Harvest/{id} 查询一条（带文件）
     * @apiDescription 查询一条收获（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Harvest> get(@PathVariable int id) {
        Harvest obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setHarvestPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_12));
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /Harvest/pageByEId 分页展示某一企业(某一基地类型)下产品或某一产品的收获（带文件）(收获时间倒序)
     * @apiDescription 分页展示某一企业(某一基地类型)下产品或某一产品的收获（带文件）(收获时间倒序)
     * @apiParam {Integer} enterpriseId 企业id（=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} harvestDateS 收获时间起点(>=)
     * @apiParam {Date} harvestDateE 收获时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} traceCode 追溯码（唯一）
     * @apiParam {Integer} productId 产品id（=）
     * @apiParam {String} productName 产品名称
     * @apiParam {BigDecimal} harvestAmount 收获数量
     * @apiParam {String} harvestAmountUnit 收获数量单位
     * @apiParam {String} harvestWay 收获方式
     * @apiParam {Date} harvestDate 收获时间
     * @apiParam {String} weather 天气状况
     * @apiGroup 收获
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<Harvest> pageByEId(@RequestParam Map<String, Object> params,
                                                  Date harvestDateS, Date harvestDateE) throws Exception {
        String orderBy = "harvest_date desc";
        Object pId = params.get("productId");
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        if (MyObjectUtil.noNullOrEmpty(pId)) {
            toMap.put("productId", pId);
        } else {
            List<Integer> pIds = productBiz.getByEIdAndBaseType(params.get("enterpriseId"),
                    params.get("baseType"));
            if (pIds != null) {
                inMap.put("productId", pIds);
            } else {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("harvestDate", harvestDateS);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("harvestDate", harvestDateE);
        TableResultResponse<Harvest> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<Harvest> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(Harvest::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_12);
            FileInfoUtil.setListField("setHarvestPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
