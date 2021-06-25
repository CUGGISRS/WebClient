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
import com.github.wxiaoqi.security.zs.sys.biz.BaseInfoBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BaseInfo;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基地
 */
@RestController
@RequestMapping("BaseInfo")
public class BaseInfoController extends BaseController<BaseInfoBiz, BaseInfo> {
    @Autowired
    private IFileInfoService fileInfoService;


    /**
     * @api {post} /BaseInfo/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条基地(带文件)信息
     * @apiParam {MultipartFile[]} reportPictureFiles 环境检验报告图片
     * @apiParam {MultipartFile[]} basePictureFiles 基地图片
     * @apiParam {String} name 名称
     * @apiParam {BigDecimal} area 面积（亩）
     * @apiParam {String} baseType 基地类型
     * @apiParam {String} address 地址
     * @apiParam {String} lender 负责人
     * @apiParam {String} lenderPhone 负责人电话
     * @apiParam {Integer} isReportQualified 环境检验报告是否合格（1是，0否）
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} isDel 是否删除（0否，1是）(默认0)
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<BaseInfo> addDataAndFile(BaseInfo baseInfo,
                                                       MultipartFile[] reportPictureFiles,
                                                       MultipartFile[] basePictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(baseInfo);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("基地添加失败");
        }
        Integer id = baseInfo.getId();
        //添加多文件
        List<FileInfo> reportPictureList = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_4,
                CommonConstants.FILE_TYPE_1, reportPictureFiles, delUrls);
        List<FileInfo> basePictureList = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_4,
                CommonConstants.FILE_TYPE_2, basePictureFiles, delUrls);
        baseInfo.setReportPictureList(reportPictureList);
        baseInfo.setBasePictureList(basePictureList);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, baseInfo);
    }

    /**
     * @api {put} /BaseInfo/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条基地(带文件)信息
     * @apiParam {MultipartFile[]} reportPictureFiles 环境检验报告图片
     * @apiParam {MultipartFile[]} basePictureFiles 基地图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {BigDecimal} area 面积（亩）
     * @apiParam {String} baseType 基地类型
     * @apiParam {String} address 地址
     * @apiParam {String} lender 负责人
     * @apiParam {String} lenderPhone 负责人电话
     * @apiParam {Integer} isReportQualified 环境检验报告是否合格（1是，0否）
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} isDel 是否删除（0否，1是）
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<BaseInfo> update(BaseInfo baseInfo,
                                               MultipartFile[] reportPictureFiles,
                                               MultipartFile[] basePictureFiles, Integer[] delFileIds
    ) throws Exception {
        Integer id = baseInfo.getId();
        if (id != null) {
            BaseInfo oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(baseInfo);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("基地修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_4
                        , CommonConstants.FILE_TYPE_1, reportPictureFiles, delUrls);
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_4,
                        CommonConstants.FILE_TYPE_2, basePictureFiles, delUrls);

                // 修改产品、加工批次中关联字段
            /*    String name=baseInfo.getName();
                if(MyObjectUtil.noNullOrEmpty(name)){
                    productBiz.updateByBaseId(new Product(name),id);
                    processBatchBiz.updateByBaseId(new ProcessBatch(name),id);
                }*/

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> fList = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_4);
                baseBiz.setFiles(baseInfo, fList);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, baseInfo);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /BaseInfo/{id}  删除一条(假)
     * @apiDescription 无
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BaseInfo> remove(@PathVariable int id) throws Exception {
        BaseInfo old = baseBiz.delFalse(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);


       /* BaseInfo old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("基地删除失败");
            }
            //删除多文件
            List<String> delUrls = new ArrayList<>();
            List<FileInfo> fileInfos = fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_4, delUrls);
            baseBiz.setFiles(old, fileInfos);
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);*/
    }

    /**
     * @api {post} /BaseInfo/batchDelete 批量删除(假)
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delFalse(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
       /* List<BaseInfo> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("基地批量删除失败");
                }
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_4, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);*/
    }


    /**
     * @api {get} /BaseInfo/{id} 查询一条（带文件）
     * @apiDescription 查询一条基地（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BaseInfo> get(@PathVariable int id) {
        BaseInfo obj = baseBiz.selectById(id);
        if (obj != null) {
            List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_4);
            baseBiz.setFiles(obj, fileInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /BaseInfo/pageByEId 分页展示某一企业的基地（带文件）(id倒序)
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {BigDecimal} area 面积（亩）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {String} address 地址
     * @apiParam {String} lender 负责人
     * @apiParam {String} lenderPhone 负责人电话
     * @apiParam {Integer} isReportQualified 环境检验报告是否合格（1是，0否）
     * @apiParam {Integer} enterpriseId 企业id（必填,=）
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} isDel 是否删除（0否，1是）(默认0,=)
     * @apiGroup 基地
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<BaseInfo> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object parentId = params.get("enterpriseId");
        Object baseType = params.get("baseType");
        if (MyObjectUtil.noNullOrEmpty(parentId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", parentId);
            andToMap.put("baseType", baseType);

            Object isDel = params.get("isDel");
            andToMap.put("isDel", isDel == null ? 0 : isDel);
            TableResultResponse<BaseInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    andToMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<BaseInfo> list = data.getData().getRows();
            if (list != null) {
                List<Integer> ids = list.stream().map(BaseInfo::getId).collect(Collectors.toList());
                //设置多文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                        CommonConstants.FORM_NAME_4);
                FileInfoUtil.setListField("setReportPictureList", fileInfos, list,
                        CommonConstants.FILE_TYPE_1);
                FileInfoUtil.setListField("setBasePictureList", fileInfos, list,
                        CommonConstants.FILE_TYPE_2);
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
