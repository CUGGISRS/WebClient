package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.FileUtils;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.config.WebAppConfigurer;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.dzsw.sys.entity.MarketingArticleInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.MarketingArticleInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TsaiJun
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MarketingArticleInfoBiz extends BaseBiz<MarketingArticleInfoMapper, MarketingArticleInfo> {

    @Autowired
    private WebAppConfigurer webAppConfigurer;

    /**
     * 修改市场行情表的内容中网络图片变为本地图片，a标签跳转url变为空
     */
    public Integer updateAllContent(){
        //获得所有信息
        List<MarketingArticleInfo>  articleInfos=selectListAll();
        int len= MyObjectUtil.iterableCount(articleInfos);
        if(len==0){
            return 0;
        }
        //需要修改的数据集合
        List<MarketingArticleInfo> updList=new ArrayList<>();
        //下载网络图片存储地址
        String folder=webAppConfigurer.getSON_PATH()+"articleImg/";
        String path=webAppConfigurer.getROOT_PATH()+folder;
        String urlPrefix="http://"+webAppConfigurer.getIp()+":"+webAppConfigurer.getPort()+folder;
        //匹配正则
        String pattern="http://[^\" ]+";
        String[] imgSuffixS={".jpg",".png",".gif",".bmp",".jpeg"};//图片url后缀

        for (int i=0;i<len;i++){
            MarketingArticleInfo item=articleInfos.get(i);
            Integer id=item.getId();
            if(item==null){
                continue;
            }
            String content=item.getContent();
            int strLen=content==null?0:content.length();
            if(strLen==0){
                continue;
            }
            //替换内容

            String newContent=  FileUtils.replaceByPattern(pattern,imgSuffixS,content,strLen,path,urlPrefix);
            if(newContent!=null){
                updList.add(new MarketingArticleInfo(id,newContent));
            }
        }
        Integer num=updList.size();

        //修改数据库
        batchUpdateSelectiveByList(updList);
        return num;
    }



    /**
     * 为指定文章增加1个访问量
     *
     * @param id
     * @return
     */
    public void addOneReading(int id) {
        MarketingArticleInfo marketingArticleInfo = selectById(id);
        try {
            // 如果阅读量reading 初始值为null
            if (marketingArticleInfo.getReading() == null) {
                marketingArticleInfo.setReading(1);
            } else {
                int reading = marketingArticleInfo.getReading() + 1;
                marketingArticleInfo.setReading(reading);
            }
            updateById(marketingArticleInfo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ObjectRestResponse getInfoById(int id) {
        try {
            MarketingArticleInfo marketingArticleInfo = selectById(id);
            addOneReading(id);
            return new ObjectRestResponse(StatusCode.SUCCESS, marketingArticleInfo);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }


    /**
     * 获取浏览量最高的市场行情
     */
    public ObjectRestResponse getMostPopularityMarketing(JSONObject jsonObject) {
        try {
            Example example = new Example(MarketingArticleInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isDeleted", 0);
            criteria.andEqualTo("type", jsonObject.get("type"));
            example.setOrderByClause("reading DESC");
//            example.setOrderByClause("pub_time DESC");
            Page<Object> result = PageHelper.startPage(1, jsonObject.getIntValue("limit"));
            List<MarketingArticleInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, jsonObject.getIntValue("limit")));
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
//        if (jsonObject.containsKey("limit")) {
//            jsonObject.put("limit", jsonObject.getIntValue("limit"));
//        } else {
//            // 如果前端没有做限制，将使用10条作为默认参数。
//            jsonObject.put("limit", 10);
//        }
//        List<JSONObject> list = mapper.getMostPopularityMarketing(jsonObject);
//        return new ObjectRestResponse(StatusCode.SUCCESS, CommonUtil.successPage(list));
    }

    /**
     * 根据农产品类型分页获取市场行情
     *
     * @param jsonObject
     * @return
     */
    public TableResultResponse listByType(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<MarketingArticleInfo> list;
//        List<DataDictionary> dicList = mapper.listDic();
        Example example = new Example(MarketingArticleInfo.class);
        Example.Criteria criteria = example.createCriteria();
        // 如果前端没有传 status 参数，则默认为所有类型的
        if (jsonObject.containsKey("status")) {
            criteria.andEqualTo("status", jsonObject.getString("status"));
        }
        // 如果有前端传了农产品分类则按照category查询，否则查全部
        if (jsonObject.containsKey("category")) {
            criteria.andEqualTo("productCategory", jsonObject.getString("category"));
        }
        // 查询删除状态为0(未删除)的数据
        criteria.andEqualTo("isDeleted", 0);
        Page<Object> result = PageHelper.startPage(jsonObject.getIntValue("pageNum"), jsonObject.getIntValue("pageRow"));
        list = selectByExample(example);
//        for (int i = 0; i < list.size(); i++) {
//            for (int j = 0; j < dicList.size(); j++) {
//                if (list.get(i).getProductCategory() == (dicList.get(j).getCode())) {
//                    list.get(i).setProductCategory(dicList.get(j).getText());
//                }
//            }
//        }
        return new TableResultResponse<>(result.getTotal(), list);
    }


    /**
     * 分页获取市场行情或者产销信息
     *
     * @return
     */
    public ObjectRestResponse listByPage(Map<String, Object> map) {
        try {
            Query query = new Query(map);

            boolean isContainContent=false;
            Example example =null;
            if(query.containsKey("isContainContent")){
              isContainContent=Boolean.valueOf(query.get("isContainContent").toString());
            }

            Map<String,Object> toMap=new HashMap<>();
            toMap.put("type",map.get("type"));
            toMap.put("isDeleted",0);
            String oderBy="pub_time DESC";
            if(!isContainContent){
                String[] columns={"id","title","productCategory","reading","source","pubTime","author","status","type","isDeleted"};
                //设置需要查询的列
                example = getExampleByConditions(columns,oderBy,query,null,null,
                        null,null,toMap,null,null,null,
                        null,null,null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,null,null);
            }else {
                example = getExampleByConditions(null,oderBy,query,null,null,
                        null,null,toMap,null,null,null,
                        null,null,null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,null,null);
            }

            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<MarketingArticleInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }
    }

    public ObjectRestResponse updateInfoById(MarketingArticleInfo marketingArticleInfo) {
        try {
            int affect = updateSelectiveById(marketingArticleInfo);
            if (affect > 0) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS, marketingArticleInfo);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }
    }

}
