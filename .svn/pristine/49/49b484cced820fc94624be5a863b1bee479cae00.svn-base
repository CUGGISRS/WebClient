package com.github.wxiaoqi.security.common.rest;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-15 8:48
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BaseController<Biz extends BaseBiz, Entity> {

    @Autowired
    protected Biz baseBiz;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity) throws Exception {
        int changed = baseBiz.insertSelective(entity);
        if(changed > 0){
            return  new ObjectRestResponse<>(StatusCode.SUCCESS,entity,true);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Entity> get(@PathVariable int id) {
        Object o = baseBiz.selectById(id);
        if (o == null) {
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
       return  new ObjectRestResponse<>(StatusCode.SUCCESS,(Entity) o,true);
    }

    @RequestMapping(value = "",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity) throws Exception {
        int changed = baseBiz.updateSelectiveById(entity);
        if(changed > 0){
            return  new ObjectRestResponse<>(StatusCode.SUCCESS,entity,true);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<Entity> remove(@PathVariable int id) throws Exception{
        Object o = baseBiz.selectById(id);
        if (o == null) {
            return  new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        int changed = baseBiz.deleteById(id);
        if(changed>0){
            return  new ObjectRestResponse<>(StatusCode.SUCCESS,(Entity) o,true);
        }
        throw new Exception("删除失败");
    }

    /**
     * 通过id集合批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        int sum= MyObjectUtil.iterableCount(ids);
        List<Entity> entities=baseBiz.batchSelectByIds(ids);
        if (entities==null||entities.size()!=sum) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        int changed = baseBiz.batchDeleteByIds(ids);
        if(changed==sum){
            return  new ObjectRestResponse<>(StatusCode.SUCCESS,changed,true);
        }
        throw new Exception("批量删除失败");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> all() {
        List<Entity> list = baseBiz.selectListAll();
        return new TableResultResponse<Entity>(list.size(), list);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> list(@RequestParam Map<String, Object> params) throws Exception {
        //查询列表数据

        return baseBiz.selectByQuery(null,null,params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }

    @RequestMapping(value = "/num", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Integer> count(@RequestParam Map<String, Object> params) throws Exception {
        //查询列表条数
        Integer count = baseBiz.countByQuery(params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,count,true);
    }

    public String getCurrentUserName() {
        return BaseContextHandler.getUsername();
    }
}
