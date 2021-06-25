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
import com.github.wxiaoqi.security.zs.sys.biz.EnterpriseQualificationBiz;
import com.github.wxiaoqi.security.zs.sys.entity.EnterpriseQualification;
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
 * 企业资质
 */
@RestController
@RequestMapping("EnterpriseQualification")
public class EnterpriseQualificationController extends BaseController<EnterpriseQualificationBiz, EnterpriseQualification> {
    @Autowired
    private IFileInfoService fileInfoService;

    /**
     * @api {post} /EnterpriseQualification/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条企业资质(带文件)信息
     * @apiParam {MultipartFile[]} aptitudeCertificateFiles 资质证书
     * @apiParam {String} aptitudeCode 企业资质代码
     * @apiParam {String} aptitudeType 企业资质类型
     * @apiParam {String} certificationUnitName 发证单位
     * @apiParam {Date} certificationTime 发证时间
     * @apiParam {Date} effectiveTime 有效时间
     * @apiParam {String} permitRange 许可范围
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<EnterpriseQualification> addDataAndFile(EnterpriseQualification obj,
                                                                      MultipartFile[] aptitudeCertificateFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("企业资质添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_9,
                CommonConstants.FILE_TYPE_1, aptitudeCertificateFiles, delUrls);
        obj.setAptitudeCertificateList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }


    /**
     * @api {put} /EnterpriseQualification/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条企业资质(带文件)信息
     * @apiParam {MultipartFile[]} aptitudeCertificateFiles 资质证书
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} aptitudeCode 企业资质代码
     * @apiParam {String} aptitudeType 企业资质类型
     * @apiParam {String} certificationUnitName 发证单位
     * @apiParam {Date} certificationTime 发证时间
     * @apiParam {Date} effectiveTime 有效时间
     * @apiParam {String} permitRange 许可范围
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<EnterpriseQualification> update(EnterpriseQualification obj,
                                                              MultipartFile[] aptitudeCertificateFiles, Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            EnterpriseQualification oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("企业资质修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_9
                        , CommonConstants.FILE_TYPE_1, aptitudeCertificateFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_9);
                obj.setAptitudeCertificateList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /EnterpriseQualification/{id}  删除一条(带文件)
     * @apiDescription 删除一条企业资质(带文件)信息
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<EnterpriseQualification> remove(@PathVariable int id) throws Exception {
        EnterpriseQualification old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("企业资质删除失败");
            }
            //删除多文件
            List<String> delUrls = new ArrayList<>();
            old.setAptitudeCertificateList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_9, delUrls));
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /EnterpriseQualification/batchDelete 批量删除(带文件)
     * @apiDescription 通过id集合批量删除企业资质(带文件)
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<EnterpriseQualification> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("企业资质批量删除失败");
                }
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_9, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /EnterpriseQualification/{id} 查询一条（带文件）
     * @apiDescription 查询一条企业资质（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */

    @Override
    public ObjectRestResponse<EnterpriseQualification> get(@PathVariable int id) {
        EnterpriseQualification obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setAptitudeCertificateList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_9));
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /EnterpriseQualification/pageByEId 分页展示某一企业的企业资质（带文件）（有效时间倒序）
     * @apiDescription 分页展示某一企业的企业资质（带文件）（有效时间倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} aptitudeCode 企业资质代码
     * @apiParam {String} aptitudeType 企业资质类型
     * @apiParam {String} certificationUnitName 发证单位
     * @apiParam {Date} certificationTime 发证时间
     * @apiParam {Date} effectiveTime 有效时间
     * @apiParam {String} permitRange 许可范围
     * @apiParam {Integer} enterpriseId 企业id(必填，=)
     * @apiParam {String} enterpriseName 企业名称
     * @apiGroup 企业资质
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<EnterpriseQualification> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "effective_time desc";
        Object parentId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(parentId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", parentId);
            TableResultResponse<EnterpriseQualification> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    andToMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<EnterpriseQualification> list = data.getData().getRows();
            if (list != null) {
                List<Integer> ids = list.stream().map(EnterpriseQualification::getId).collect(Collectors.toList());
                //设置多文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                        CommonConstants.FORM_NAME_9);
                FileInfoUtil.setListField("setAptitudeCertificateList", fileInfos, list,
                        CommonConstants.FILE_TYPE_1);
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

}
