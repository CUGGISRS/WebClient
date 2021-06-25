package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.UserBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.User;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.msg.TableResultResponse;
import com.zd.earth.manage.util.StringHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "用户")
@RestController
@RequestMapping("User")
public class UserRest extends BaseRest<UserBiz, User> {

    @ApiOperation("添加一条(用户名唯一，且用户名、密码非空)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "User"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型（1管理员、2普通用户）", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "phone", value = "联系电话", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "ip", value = "ip地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitName", value = "单位名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitAddress", value = "单位地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态(0禁用，1启用)", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "mailbox", value = "邮箱", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", paramType = "query",dataType = "Date")
    })
    @Override
    public ObjectRestResponse<User> add(@RequestBody User obj) throws Exception {
        //能否添加
        String errorInfo= baseBiz.isAdd(obj);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return super.add(obj);
    }

    @ApiOperation("修改一条(密码不修改，用户名唯一)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "User"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型（1管理员、2普通用户）", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "phone", value = "联系电话", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "ip", value = "ip地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitName", value = "单位名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitAddress", value = "单位地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态(0禁用，1启用)", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "mailbox", value = "邮箱", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", paramType = "query",dataType = "Date")
    })
    @Override
    public ObjectRestResponse<User> update(@RequestBody User obj) throws Exception {
        Integer id=obj.getId();
        User old=baseBiz.selectById(id);
        if(old!=null){
            //是否唯一
            String username=obj.getUsername();
            User result=baseBiz.getOneByUsername(username);
            if(result!=null&&!id.equals(result.getId())){
                return  new ObjectRestResponse<>(StatusCode.FAIL,"用户已存在");
            }
            //将密码设为null来控制其不能修改
            obj.setPassword(null);
            return super.update(obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "Map"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "oldPassword", value = "原密码", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query",dataType = "String")
    })
    @PutMapping("updatePWD")
    public ObjectRestResponse updatePWD(@RequestBody Map<String, String> map) {

        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        String username = map.get("username");
        if (StringHelper.isNullOrEmpty(oldPassword) || StringHelper.isNullOrEmpty(newPassword)||StringHelper.isNullOrEmpty(username)) {
            return new ObjectRestResponse(StatusCode.FAIL, "用户名、原密码、新密码不能为空", false);
        }
        if (newPassword.equals(oldPassword)) {
            return new ObjectRestResponse(StatusCode.FAIL, "新密码不能与原密码相同", false);
        }

        User user=baseBiz.getOneByUsername(username);
        if (user == null) {
            return new ObjectRestResponse(StatusCode.FAIL, "该用户名不存在", false);
        }
        if (oldPassword.equals(user.getPassword())) {
            int index=baseBiz.updByUsername(new User(null,newPassword),username);
            if(index>0){
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
            }
            return new ObjectRestResponse(StatusCode.FAIL, "修改密码失败", false);
        }
        return new ObjectRestResponse(StatusCode.FAIL, "用户名和原密码不匹配", false);
    }

    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "编号", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<User> remove(@PathVariable int id) throws Exception {
        return super.remove(id);
    }

    @ApiOperation("批量删除")
    @ApiImplicitParam(name = "ids", value = "编号集合", paramType = "body",dataType = "int",allowMultiple = true)
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        return super.batchDeleteByIds(ids);
    }

    @ApiOperation(value = "分页展示(密码不查询，创建时间倒序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页个数",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型（1管理员、2普通用户）", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "phone", value = "联系电话", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "ip", value = "ip地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitName", value = "单位名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unitAddress", value = "单位地址", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态(0禁用，1启用)", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "mailbox", value = "邮箱", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", paramType = "query",dataType = "Date")
    })
    @GetMapping("pageByCondition")
    public TableResultResponse<User> list(@RequestParam
                                                @ApiIgnore  //该注解使swagger不展示该参数
                                                        Map<String, Object> params) throws Exception {
        String orderBy="create_time desc";
        String[] columns={"id","name","username","type","phone","ip","unitName","unitAddress","status","contactPerson","mailbox","createTime"};
        return baseBiz.selectByQuery(columns,orderBy,params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }
}
