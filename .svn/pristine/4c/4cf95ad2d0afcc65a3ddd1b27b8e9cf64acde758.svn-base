package com.github.wxiaoqi.security.admin.rest;

import com.github.wxiaoqi.security.admin.biz.MenuBiz;
import com.github.wxiaoqi.security.admin.biz.UserBiz;
import com.github.wxiaoqi.security.admin.entity.Menu;
import com.github.wxiaoqi.security.admin.entity.User;
import com.github.wxiaoqi.security.admin.rpc.service.PermissionService;
import com.github.wxiaoqi.security.admin.vo.FrontUser;
import com.github.wxiaoqi.security.admin.vo.MenuTree;
import com.github.wxiaoqi.security.auth.client.annotation.UserToken;
import com.github.wxiaoqi.security.auth.common.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserBiz,User> {
    @Autowired
    private PermissionService permissionService;
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private MenuBiz menuBiz;
    /**
     * 获取登陆人员的部门人员信息
     */
    @RequestMapping(value = "/getDeptPersonByUsername", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<User>> getDeptPersonByUsername(@UserToken IJWTInfo ijwtInfo){
        List<User> userList=baseBiz.getDeptPersonByUsername(ijwtInfo.getUniqueName());
        if(userList==null){
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,userList,true);
    }

    /**
     * 获取登陆人员的公司人员信息
     */
    @RequestMapping(value = "/getEnterprisePersonByUsername", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<User>> getEnterprisePersonByUsername(@UserToken IJWTInfo ijwtInfo){
        List<User> userList=baseBiz.getEnterprisePersonByUsername(ijwtInfo.getUniqueName());
        if(userList==null){
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,userList,true);
    }

    /**
     * 获取登陆人员连表信息
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<User> get(@UserToken IJWTInfo ijwtInfo){
        if(ijwtInfo.getId()==null){
            return  new ObjectRestResponse<>(StatusCode.FAIL,false);
        }
        Integer id=Integer.valueOf(ijwtInfo.getId());
        User user = baseBiz.getLinkOneById(id);
        if (user == null) {
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,user,true);
    }

    /**
     * 通过id连表查询一条数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/getLinkById", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<User> get(Integer id) {
        User user = baseBiz.getLinkOneById(id);
        if (user == null) {
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,user,true);
    }
    /**
     * 连表通过用户名称模糊查询后分页展示数据或分页展示全部数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/linkPage", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> linkPage(@RequestParam Map<String, Object> params) {
        return baseBiz.getLinkListByPage(params);
    }

    @ResponseBody
    @Override
    public ObjectRestResponse<User> add(@RequestBody User user) throws Exception {
        String username=user.getUsername();
        String password =user.getPassword();
        if(username==null||password==null||"".equals(username)||"".equals(password)){
            return  new ObjectRestResponse(StatusCode.FAIL,"用户名和密码不能为空",false);
        }
        User user1=new User();
        user1.setUsername(username);
        //username不能重复
        if(baseBiz.selectCount(user1)!=0){
            return  new ObjectRestResponse(StatusCode.FAIL,"用户名已存在",false);
        }
        user.setPassword(encoder.encode(password));
        return super.add(user);
    }

    /**
     * 修改用户密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<User> updatePassword(@RequestBody Map<String,String> map) throws Exception {
        String username=map.get("username");
        String oldPassword=map.get("oldPassword");
        String newPassword=map.get("newPassword");
        if(username==null||"".equals(username.trim())){
            return  new ObjectRestResponse(StatusCode.FAIL,"用户名不能为空",false);
        }
        if(oldPassword==null||"".equals(oldPassword.trim())){
            return  new ObjectRestResponse(StatusCode.FAIL,"密码不能为空",false);
        }
        if(newPassword==null||"".equals(newPassword.trim())){
            return  new ObjectRestResponse(StatusCode.FAIL,"新密码不能为空",false);
        }
        if(newPassword.equals(oldPassword)){
            return  new ObjectRestResponse(StatusCode.FAIL,"新密码不能与原密码相同",false);
        }
        User user0 = baseBiz.getUserByUsername(username);
        if(user0==null){
            return  new ObjectRestResponse(StatusCode.FAIL,"用户名或密码错误",false);
        }
        if (encoder.matches(oldPassword, user0.getPassword())) {
           User user=new User();
           user.setId(user0.getId());
           user.setUsername(user0.getUsername());
           user.setPassword(encoder.encode(newPassword));
           return super.update(user);
        }
        return  new ObjectRestResponse(StatusCode.FAIL,"用户名或密码错误",false);

    }


    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(String token) throws Exception {
        FrontUser userInfo = permissionService.getUserInfo(token);
        if(userInfo==null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }


    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    public @ResponseBody
    List<MenuTree> getMenusByUsername(String token) throws Exception {
        return permissionService.getMenusByUsername(token);
    }

    @RequestMapping(value = "/front/menu/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Menu> getAllMenus() throws Exception {
        return menuBiz.selectListAll();
    }
}
