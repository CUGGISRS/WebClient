package com.github.wxiaoqi.security.zs.sys.rest;

import com.aliyuncs.IAcsClient;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import com.github.wxiaoqi.security.zs.sys.biz.*;
import com.github.wxiaoqi.security.zs.sys.config.MyWebMvcConfigurer;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.feign.service.IUserService;
import com.github.wxiaoqi.security.zs.sys.util.SendSmsUtil;
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
 * 企业
 */
@RestController
@RequestMapping("Enterprise")
public class EnterpriseController extends BaseController<EnterpriseBiz, Enterprise> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private IUserService userService;
    @Autowired
    private BetweenEnterpriseUserBiz betweenEnterpriseUserBiz;

    @Autowired
    private MyWebMvcConfigurer myWebMvcConfigurer;
    @Autowired
    private IAcsClient iAcsClient;

    @GetMapping("getOneByUserId")
    public UserInfo getOneByUserId(Integer userId) throws Exception {
        Enterprise enterprise = baseBiz.getOneByUserId(userId);
        if (enterprise == null) {
            return new UserInfo();
        }
        return new UserInfo(enterprise.getId(), enterprise.getIsViewpoint());
    }

    /**
     * @api {post} /Enterprise/addDataAndFile  添加一条(带文件)、企业-用户、用户
     * @apiDescription 添加一条企业(带文件)信息、企业-用户、用户
     * @apiParam {String} username 用户名(必填)
     * @apiParam {String} password 密码(必填)
     * @apiParam {String} sysName 系统名称(必填)
     * @apiParam {MultipartFile[]} businessLicenseFiles 营业执照图片
     * @apiParam {MultipartFile[]} deputyIdCardPhotoFiles 法定代表人身份证照片
     * @apiParam {MultipartFile[]} enterpriseImageFiles 企业形象图
     * @apiParam {String} name 企业名称
     * @apiParam {String} businessType 经营类型
     * @apiParam {String} socialCode 统一社会信用代码
     * @apiParam {String} district 行政区规划
     * @apiParam {String} organizationForm 组织形式
     * @apiParam {String} subjectAttribute 主体属性
     * @apiParam {String} registerAddress 注册地址
     * @apiParam {Date} establishDate 成立日期
     * @apiParam {BigDecimal} registerMoney 注册资金（万元）
     * @apiParam {String} contactPerson 联系人
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} postcode 邮政编码
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {String} deputyName 法定代表人姓名
     * @apiParam {String} deputyIdCard 法定代表人身份证号
     * @apiParam {String} deputyPhone 法定代表人联系电话
     * @apiParam {Integer} creditLevel 信用等级(星级分为1-5)
     * @apiParam {Integer} status 状态(0待审核，1正常)
     * @apiParam {Integer} wlUserId 物联网用户id
     * @apiParam {String} wlUsername 物联网用户名
     * @apiParam {String} wlPassword 物联网密码
     * @apiParam {Integer} isViewpoint 是否为视点（1是，0否）
     * @apiParam {String} evaluateGrade 评价等级（优、良、中、差）
     * @apiParam {Date} evaluateTime 评价时间
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<Enterprise> addDataAndFile(Enterprise obj,
                                                         MultipartFile[] businessLicenseFiles,
                                                         MultipartFile[] deputyIdCardPhotoFiles,
                                                         MultipartFile[] enterpriseImageFiles,
                                                         String username,
                                                         String password,
                                                         String sysName) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            throw new Exception("企业添加失败");
        }
        Integer id = obj.getId();
        //创建用户
        User user = userService.createUser(new User(obj.getName(), username, password, 2, sysName));
        int i = betweenEnterpriseUserBiz.insertSelective(new BetweenEnterpriseUser(id, user.getId()));
        if (i == 0) {
            throw new Exception("企业-用户添加失败");
        }

        obj = baseBiz.selectById(id);
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8,
                CommonConstants.FILE_TYPE_1, businessLicenseFiles, delUrls);
        obj.setBusinessLicenseList(list1);
        List<FileInfo> list2 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8,
                CommonConstants.FILE_TYPE_2, deputyIdCardPhotoFiles, delUrls);
        obj.setDeputyIdCardPhotoList(list2);
        List<FileInfo> list3 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8,
                CommonConstants.FILE_TYPE_3, enterpriseImageFiles, delUrls);
        obj.setEnterpriseImageList(list3);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /Enterprise/updateDataAndFile  修改一条(带文件)(、用户)
     * @apiDescription 修改一条企业(带文件)信息(、 用户)
     * @apiParam {MultipartFile[]} businessLicenseFiles 营业执照图片
     * @apiParam {MultipartFile[]} deputyIdCardPhotoFiles 法定代表人身份证照片
     * @apiParam {MultipartFile[]} enterpriseImageFiles 企业形象图
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 企业名称
     * @apiParam {String} businessType 经营类型
     * @apiParam {String} socialCode 统一社会信用代码
     * @apiParam {String} district 行政区规划
     * @apiParam {String} organizationForm 组织形式
     * @apiParam {String} subjectAttribute 主体属性
     * @apiParam {String} registerAddress 注册地址
     * @apiParam {Date} establishDate 成立日期
     * @apiParam {BigDecimal} registerMoney 注册资金（万元）
     * @apiParam {String} contactPerson 联系人
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} postcode 邮政编码
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {String} deputyName 法定代表人姓名
     * @apiParam {String} deputyIdCard 法定代表人身份证号
     * @apiParam {String} deputyPhone 法定代表人联系电话
     * @apiParam {Integer} creditLevel 信用等级(星级分为1-5)
     * @apiParam {Integer} status 状态(0待审核，1正常)（若修改状态则同时修改用户表状态）
     * @apiParam {Integer} wlUserId 物联网用户id
     * @apiParam {String} wlUsername 物联网用户名
     * @apiParam {String} wlPassword 物联网密码
     * @apiParam {Integer} isViewpoint 是否为视点（1是，0否）
     * @apiParam {String} evaluateGrade 评价等级（优、良、中、差）
     * @apiParam {Date} evaluateTime 评价时间
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<Enterprise> update(Enterprise obj,
                                                 MultipartFile[] businessLicenseFiles,
                                                 MultipartFile[] deputyIdCardPhotoFiles,
                                                 MultipartFile[] enterpriseImageFiles,
                                                 Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            Enterprise oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                Integer status = obj.getStatus();
                if (MyObjectUtil.noNullOrEmpty(status)) {
                    //获得企业级用户，一个企业应该只有一个企业级用户
                    List<User> users = betweenEnterpriseUserBiz.getUsersByEId(id, 2);
                    if (users == null || users.size() > 1) {
                        throw new Exception("该企业不存在用户信息或存在超过一个企业级用户");
                    }
                    //同时修改用户状态
                    userService.updateUser(new User(users.get(0).getId(), status));

                }

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("企业修改失败");
                }
                obj = baseBiz.selectById(id);

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8
                        , CommonConstants.FILE_TYPE_1, businessLicenseFiles, delUrls);
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8
                        , CommonConstants.FILE_TYPE_2, deputyIdCardPhotoFiles, delUrls);
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_8
                        , CommonConstants.FILE_TYPE_3, enterpriseImageFiles, delUrls);

                //修改基地、收购、企业资质、产品详情、销售商的关联字段
             /*   String name=obj.getName();
                if(MyObjectUtil.noNullOrEmpty(name)){
                    baseInfoBiz.updateByEId(new BaseInfo(name),id);
                    buyInfoBiz.updateByEId(new BuyInfo(null,name),id);
                    enterpriseQualificationBiz.updateByEId(new EnterpriseQualification(name),id);
                    productDetailBiz.updateByEId(new ProductDetail(name),id);
                    sellerBiz.updateByEId(new Seller(name),id);
                }*/

                //回填文件数据
                List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_8);
                setFiles(obj, fileInfos);
                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /Enterprise/{id}  删除一条(带文件)、企业-用户、用户(带文件)
     * @apiDescription 删除一条企业(带文件)信息、企业-用户、用户(带文件)
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @Override
    public ObjectRestResponse<Enterprise> remove(@PathVariable int id) throws Exception {
        Enterprise old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("企业删除失败");
            }
            //删除多文件
            List<String> delUrls = new ArrayList<>();
            List<FileInfo> fileInfos = fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_8, delUrls);
            setFiles(old, fileInfos);

            //同时删除企业-用户、用户(用户文件存入集合)
            List<BetweenEnterpriseUser> beu = betweenEnterpriseUserBiz.getByEId(id);
            betweenEnterpriseUserBiz.delBEUAndUser(beu,delUrls);

            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /Enterprise/batchDelete 批量删除(带文件)、企业-用户、用户(带文件)
     * @apiDescription 通过id集合批量删除企业(带文件)、企业-用户、用户(带文件)
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */

    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<Enterprise> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("企业批量删除失败");
                }
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_8, delUrls);

                //同时删除、企业-用户、用户(带文件)
                List<BetweenEnterpriseUser> beu = betweenEnterpriseUserBiz.getByEIds(ids);
                betweenEnterpriseUserBiz.delBEUAndUser(beu,delUrls);

                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /Enterprise/{id} 查询一条（带文件）
     * @apiDescription 查询一条企业（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Enterprise> get(@PathVariable int id) {
        Enterprise obj = baseBiz.selectById(id);
        if (obj != null) {
            List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_8);
            setFiles(obj, fileInfos);

            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /Enterprise/pageByCondition 分页展示企业（带文件）（id倒序）
     * @apiDescription 分页展示企业（带文件）（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 企业名称
     * @apiParam {String} businessType 经营类型
     * @apiParam {String} socialCode 统一社会信用代码
     * @apiParam {String} district 行政区规划
     * @apiParam {String} organizationForm 组织形式
     * @apiParam {String} subjectAttribute 主体属性
     * @apiParam {String} registerAddress 注册地址
     * @apiParam {Date} establishDate 成立日期
     * @apiParam {BigDecimal} registerMoney 注册资金（万元）
     * @apiParam {String} contactPerson 联系人
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} postcode 邮政编码
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {String} deputyName 法定代表人姓名
     * @apiParam {String} deputyIdCard 法定代表人身份证号
     * @apiParam {String} deputyPhone 法定代表人联系电话
     * @apiParam {Integer} creditLevel 信用等级(星级分为1-5)
     * @apiParam {Integer} status 状态(0待审核，1正常)
     * @apiParam {Integer} wlUserId 物联网用户id
     * @apiParam {String} wlUsername 物联网用户名
     * @apiParam {String} wlPassword 物联网密码
     * @apiParam {Integer} isViewpoint 是否为视点（1是，0否）
     * @apiParam {String} evaluateGrade 评价等级（优、良、中、差、未评价）（=）
     * @apiParam {Date} evaluateTime 评价时间
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCondition")
    public TableResultResponse<Enterprise> pageByCondition(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";

        Object evaluateGrade=params.get("evaluateGrade");
        Map<String,Object> toMap=new HashMap<>();
        Map<String,Iterable> orNotInMap=new HashMap<>();
        List<String> orNullList=new ArrayList<>();
        if(evaluateGrade!=null){
            String evaluateGradeStr=evaluateGrade.toString();
            if("未评价".equals(evaluateGradeStr)){
                List<String> egS=new ArrayList<>();
                egS.add("QPD1");
                egS.add("QPD2");
                egS.add("QPD3");
                egS.add("QPD4");
                orNotInMap.put("evaluateGrade",egS);
                orNullList.add("evaluateGrade");
            }else {
                toMap.put("evaluateGrade",evaluateGradeStr);
            }
        }

        TableResultResponse<Enterprise> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, null, null, null, null, null,
                null, null, orNullList, null, null, null,
                null, null, null, null, null, orNotInMap);
        ;
        List<Enterprise> list = data.getData().getRows();

        if (list != null) {
            List<Integer> ids = list.stream().map(Enterprise::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_8);
            FileInfoUtil.setListField("setBusinessLicenseList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            FileInfoUtil.setListField("setDeputyIdCardPhotoList", fileInfos, list,
                    CommonConstants.FILE_TYPE_2);
            FileInfoUtil.setListField("setEnterpriseImageList", fileInfos, list,
                    CommonConstants.FILE_TYPE_3);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * @api {get} /Enterprise/pageViewpoint 分页展示作为视点的企业（id倒序）
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageViewpoint")
    public TableResultResponse<Enterprise> pageViewpoint(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("isViewpoint", 1);
        TableResultResponse<Enterprise> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<Enterprise> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }


    /**
     * @api {get} /Enterprise/sendSms 发送企业审核短信
     * @apiDescription 无
     * @apiParam {String} phone 收信人手机号(非空)
     * @apiParam {String} reason 未通过理由(非空)
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("sendSms")
    public ObjectRestResponse sendSms(String phone, String reason) throws Exception {

        if (StringHelper.isNullOrEmpty(phone) || StringHelper.isNullOrEmpty(reason)) {
            return new ObjectRestResponse(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("reason", reason);
        boolean r = SendSmsUtil.sendSms(phone, myWebMvcConfigurer.getSignName(),
                myWebMvcConfigurer.getTemplateCode1(), myWebMvcConfigurer.getAction()
                , myWebMvcConfigurer.getVersion(), myWebMvcConfigurer.getRegionId(), myWebMvcConfigurer.getDomain()
                , iAcsClient, params);
        if (r) {
            return new ObjectRestResponse(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse(StatusCode.FAIL, false);
    }

    /**
     * @api {get} /Enterprise/sendPromptSms 向监督管理员发送企业注册成功提示短信
     * @apiDescription 无
     * @apiParam {String} name 企业名称(非空)
     * @apiGroup 企业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("sendPromptSms")
    public ObjectRestResponse sendPromptSms(String name) throws Exception {

        if (StringHelper.isNullOrEmpty(name)) {
            return new ObjectRestResponse(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        //获得监督管理员的手机号
        List<Integer> userIds=new ArrayList<>();
        userIds.add(1);
        List<User> users=userService.selectByIds(userIds);
        String phone=null;
        if(users!=null&&users.size()==1){
           phone=users.get(0).getPhone();
        }
        if (StringHelper.isNullOrEmpty(phone)) {
            return new ObjectRestResponse(StatusCode.FAIL, "管理员手机号获取失败");
        }
        boolean r = SendSmsUtil.sendSms(phone, myWebMvcConfigurer.getSignName(),
                myWebMvcConfigurer.getTemplateCode2(), myWebMvcConfigurer.getAction()
                , myWebMvcConfigurer.getVersion(), myWebMvcConfigurer.getRegionId(), myWebMvcConfigurer.getDomain()
                , iAcsClient, params);
        if (r) {
            return new ObjectRestResponse(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse(StatusCode.FAIL, false);
    }


    /**
     * 设置文件属性值
     */
    private void setFiles(Enterprise obj, List<FileInfo> fileInfos) {
        if (obj != null && fileInfos != null && fileInfos.size() > 0) {
            obj.setBusinessLicenseList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_1.equals(v.getType())
            ).collect(Collectors.toList()));
            obj.setDeputyIdCardPhotoList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_2.equals(v.getType())
            ).collect(Collectors.toList()));
            obj.setEnterpriseImageList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_3.equals(v.getType())
            ).collect(Collectors.toList()));
        }
    }

}
