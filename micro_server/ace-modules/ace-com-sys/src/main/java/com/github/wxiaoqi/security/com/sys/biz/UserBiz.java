package com.github.wxiaoqi.security.com.sys.biz;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.com.sys.feign.service.IBetweenDeptUserService;
import com.github.wxiaoqi.security.com.sys.feign.service.IBetweenEnterpriseUserService;
import com.github.wxiaoqi.security.com.sys.mapper.UserMapper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MD5Util;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户
 */
@Service
public class UserBiz extends BaseBiz<UserMapper, User> {
    @Autowired
    private FileInfoBiz fileInfoBiz;
    @Autowired
    private IBetweenDeptUserService deptUserService;
    @Autowired
    private IBetweenEnterpriseUserService enterpriseUserService;



    /**
     * 通过系统名和用户名获得一条数据
     */
    public User getOneByUserNameAndSysName(String username, String sysName) {
        if (MyObjectUtil.noNullOrEmpty(username) && MyObjectUtil.noNullOrEmpty(sysName)) {
            List<User> list = getByUserNameAndSysName(username, sysName);
            if (MyObjectUtil.iterableCount(list) == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 通过系统名(like)和用户名(=)查询数据
     *
     * @param username
     * @param sysName
     * @return
     */
    public List<User> getByUserNameAndSysName(String username, String sysName) {
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("username", username);
        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put("sysName", sysName);
        return getMayCondition(null, null, likeMap, null, null, null,
                toMap, null, null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }


    /**
     * 批量创建用户
     */
    public Integer batchCreateUser(List<User> users) throws Exception {
        int sum = MyObjectUtil.iterableCount(users);
        if (sum > 0) {
            for (int i = 0, len = users.size(); i < len; i++) {
                User user = users.get(i);
                String username = user.getUsername();
                String sysName = user.getSysName();
                String password = user.getPassword();
                if (!MyObjectUtil.noNullOrEmpty(password) || !MyObjectUtil.noNullOrEmpty(username) ||
                        !MyObjectUtil.noNullOrEmpty(sysName)) {
                    throw new Exception("填入数据存在空值");
                }
                if (MyObjectUtil.iterableCount(getByUserNameAndSysName(username, sysName)) > 0) {
                    throw new Exception("系统中该用户名已存在");
                }
                //加密
                // user.setPassword(MD5Util.convertMD5(MD5Util.string2MD5(password)));
            }
            int index = batchInsertByList(users);
            if (sum != index) {
                throw new Exception("批量创建用户失败");
            }
        }
        return sum;
    }

    /**
     * 创建用户
     */
    public User createUser(User user) throws Exception {
        String username = user.getUsername();
        String sysName = user.getSysName();
        String password = user.getPassword();
        if (MyObjectUtil.noNullOrEmpty(password) &&
                MyObjectUtil.noNullOrEmpty(username) &&
                MyObjectUtil.noNullOrEmpty(sysName)) {
            if (MyObjectUtil.iterableCount(getByUserNameAndSysName(username, sysName)) > 0) {
                throw new Exception("系统中该用户名已存在");
            }
            //加密
            // user.setPassword(MD5Util.convertMD5(MD5Util.string2MD5(password)));
            int index = insertSelective(user);
            if (index == 0) {
                throw new Exception("创建用户失败");
            }
            return selectById(user.getId());
        }
        throw new Exception("填入数据存在空值");
    }


    /**
     * 修改用户
     */
    public User updateUser(User user) throws Exception {
        String username = user.getUsername();
        String sysName = user.getSysName();
        Integer id = user.getId();
    /*   String password = user.getPassword();
        if (MyObjectUtil.noNullOrEmpty(password)) {
            user.setPassword(MD5Util.convertMD5(MD5Util.string2MD5(password)));
        }*/
        User obj = selectById(id);
        if (obj == null) {
            throw new Exception("该用户id不存在");
        }
        int num = 0;
        if (!MyObjectUtil.noNullOrEmpty(username)) {
            username = obj.getUsername();
            num++;
        }
        if (!MyObjectUtil.noNullOrEmpty(sysName)) {
            sysName = obj.getSysName();
            num++;
        }
        if (num != 2 && MyObjectUtil.iterableCount(getByUserNameAndSysName(username, sysName)) > 0) {
            throw new Exception("系统中该用户名已存在");
        }
        int index = updateSelectiveById(user);
        if (index == 0) {
            throw new Exception("修改用户失败");
        }
        return selectById(id);
    }

    /**
     * 给某一用户上传或更换用户头像，并修改用户信息,返回头像url
     */
    public String uploadUserPhoto(MultipartFile file, Integer id) throws Exception {

        User user = selectById(id);
        if (user != null) {
            String photo = fileInfoBiz.uploadFile(file, CommonConstants.FORM_NAME_21, null);
            if (photo != null) {
                int index = updateSelectiveById(new User(id, photo));
                if (index == 0) {
                    //删除新头像
                    fileInfoBiz.delFileByUrl(photo);
                    throw new Exception("用户头像路径修改失败");
                }
                //删除原头像文件
                fileInfoBiz.delFileByUrl(user.getPhoto());
                return photo;
            }
        }
        return null;
    }



    /**
     * 通过id集合删除数据和文件,企业_用户或部门_用户
     */
    public Integer delLinkDataAndFile(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<User> users = batchSelectByIds(ids);
        if (users != null && users.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (sum != index) {
                throw new Exception("批量删除用户失败");
            }
            List<String> photos = new ArrayList<>();
            List<Integer> eUserIds = null;
            List<Integer> dUserIds = null;
            for (int i = 0; i < sum; i++) {
                User item = users.get(i);
                String photo = item.getPhoto();
                photos.add(photo);

                Integer id = item.getId();
                Integer type = item.getType();
                if (CommonConstants.USER_TYPE_2.equals(type)) {
                    if (eUserIds == null) {
                        eUserIds = new ArrayList<>();
                    }
                    //企业用户
                    eUserIds.add(id);
                } else if (CommonConstants.USER_TYPE_3.equals(type)) {
                    if (dUserIds == null) {
                        dUserIds = new ArrayList<>();
                    }
                    //部门用户
                    dUserIds.add(id);
                }
            }

            //删除企业用户
            enterpriseUserService.delByUserIds(eUserIds);
            //删除部门用户
            deptUserService.delByUserIds(dUserIds);

            //删除原头像文件
            fileInfoBiz.delFileByUrls(photos);
            return sum;
        }
        return null;
    }

    /**
     * 通过id删除数据和文件,企业_用户或部门_用户
     */
    public User delLinkOneAndFile(Integer id) throws Exception {
        User user = selectById(id);
        if (user != null) {
            int index = deleteById(id);
            if (index == 0) {
                throw new Exception("删除用户失败");
            }
            Integer type = user.getType();
            if (CommonConstants.USER_TYPE_2.equals(type)) {
                //删除企业用户
                enterpriseUserService.delByUserId(id);
            } else if (CommonConstants.USER_TYPE_3.equals(type)) {
                //删除部门用户
                deptUserService.delByUserId(id);
            }
            //删除原头像文件
            String photo = user.getPhoto();
            fileInfoBiz.delFileByUrl(photo);
            return user;
        }
        return null;
    }





}
