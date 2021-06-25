package com.github.wxiaoqi.security.com.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.biz.UserBiz;
import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MD5Util;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@RestController
@RequestMapping("User")
public class UserController extends BaseController<UserBiz, User> {

    @PostMapping("selectByIds")
    public List<User> selectByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchSelectByIds(ids);
    }

    @PostMapping("delByIds")
    public Integer delByIds(@RequestBody List<Integer> ids)  {
        return baseBiz.batchDeleteByIds(ids);
    }
    
    @PostMapping(value = "batchCreateUser")
    public Integer batchCreateUser(@RequestBody List<User> users) throws Exception {
        return baseBiz.batchCreateUser(users);
    }


    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PostMapping(value = "createUser")
    public User createUser(@RequestBody User user) throws Exception {
        return baseBiz.createUser(user);
    }


    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PutMapping(value = "updateUser")
    public User updateUser(@RequestBody User user) throws Exception {
        return baseBiz.updateUser(user);
    }


    /**
     * @api {post} /User  添加一条
     * @apiDescription 添加一条用户信息
     * @apiParam {String} name 名称
     * @apiParam {String} username 用户名（必填）
     * @apiParam {String} password 密码（必填）
     * @apiParam {String} sysName 系统名称（必填）
     * @apiParam {Integer} type 类型（1个人用户、2企业用户、3部门用户）
     * @apiParam {Date} birthday 生日
     * @apiParam {String} address 地址
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} sex 性别
     * @apiParam {Integer} status 状态(0待审核，1正常，2禁用)
     * @apiParam {String} description 描述
     * @apiParam {Integer} isVerify 是否认证(0未1已)
     * @apiParam {String} photo 用户头像
     * @apiParam {String} targetSystem 目标系统
     * @apiParam {Date} regTime 注册时间
     * @apiParam {Integer} isDeleted 是否删除
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * {
     * "name":"xxx"
     * }
     * @apiGroup 用户
     * @apiVersion 1.0.0
     */
    @Override
    @ResponseBody
    public ObjectRestResponse<User> add(@RequestBody User user) throws Exception {
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, baseBiz.createUser(user));
    }

    /**
     * @api {put} /User  修改一条
     * @apiDescription 通过id修改一条用户信息
     * @apiParam {Integer} id  编号
     * @apiParam {String} name 名称
     * @apiParam {String} username 用户名
     * @apiParam {String} password 密码
     * @apiParam {Integer} type 类型（1个人用户、2企业用户、3部门用户）
     * @apiParam {Date} birthday 生日
     * @apiParam {String} address 地址
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} sex 性别
     * @apiParam {Integer} status 状态(0待审核，1正常，2禁用)
     * @apiParam {String} description 描述
     * @apiParam {Integer} isVerify 是否认证(0未1已)
     * @apiParam {String} photo 用户头像
     * @apiParam {String} sysName 系统名称
     * @apiParam {String} targetSystem 目标系统
     * @apiParam {Date} regTime 注册时间
     * @apiParam {Integer} isDeleted 是否删除
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * {
     * "id":1,"name":"xxx"
     * }
     * @apiGroup 用户
     * @apiVersion 1.0.0
     */
    @Override
    @ResponseBody
    public ObjectRestResponse<User> update(@RequestBody User user) throws Exception {
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, baseBiz.updateUser(user));
    }

    /**
     * @api {delete} /User/{id}  删除一条(带文件),企业_用户或部门_用户
     * @apiDescription 无
     * @apiGroup 用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<User> remove(@PathVariable int id) throws Exception {
        User user = baseBiz.delLinkOneAndFile(id);
        if (user == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, user);
    }

    /**
     * @api {post} /User/batchDelete 批量删除(带文件)，企业_用户或部门_用户
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer index = baseBiz.delLinkDataAndFile(ids);
        if (index == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, index);
    }

    /**
     * @api {get} /User/pageByIds 分页展示某些用户（id倒序）
     * @apiDescription 分页展示某些的用户（id倒序）
     * @apiParam {List:Integer} ids 主键集合（必填，in）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} username 用户名
     * @apiParam {String} password 密码
     * @apiParam {Integer} type 类型（1个人用户、2企业用户、3部门用户）
     * @apiParam {Date} birthday 生日
     * @apiParam {String} address 地址
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} sex 性别
     * @apiParam {Integer} status 状态(0待审核，1正常，2禁用)
     * @apiParam {String} description 描述
     * @apiParam {Integer} isVerify 是否认证(0未1已)
     * @apiParam {String} photo 用户头像
     * @apiParam {String} sysName 系统名称
     * @apiParam {String} targetSystem 目标系统
     * @apiParam {Date} regTime 注册时间
     * @apiParam {Integer} isDeleted 是否删除
     * @apiGroup 用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByIds")
    public TableResultResponse<User> pageByIds(@RequestParam Map<String, Object> params,
                                               @RequestBody List<Integer> ids) throws Exception {
        String orderBy = "id desc";
        if (ids != null && ids.size() > 0) {
            Map<String, Iterable> inMap = new HashMap<>();
            inMap.put("id", ids);
            TableResultResponse<User> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    null, null, null, null, null, null, inMap, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<User> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

}
