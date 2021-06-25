package com.github.wxiaoqi.security.info.sys.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.FileUtils;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.info.sys.config.system.MyWebMvcConfigurer;
import com.github.wxiaoqi.security.info.sys.entity.ArticleAndSystem;
import com.github.wxiaoqi.security.info.sys.entity.ArticleInfo;
import com.github.wxiaoqi.security.info.sys.entity.ArticleSystemInfo;
import com.github.wxiaoqi.security.info.sys.entity.EsArticleInfo;
import com.github.wxiaoqi.security.info.sys.mapper.ArticleInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.ArticleSystemInfoMapper;
import com.github.wxiaoqi.security.info.sys.service.EsArticleService;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.delete.DeleteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleInfoBiz extends BaseBiz<ArticleInfoMapper, ArticleInfo> {

    @Value("${zdlc.elasticsearch.key_Article}")
    private String key;
    @Autowired
    private MyWebMvcConfigurer myWebMvcConfigurer;

    public static final String[] allSysTypes = {"公益性系统", "电子商务系统", "追溯信息系统"};

    /**
     * 修改文章表的内容中网络图片变为本地图片，a标签跳转url变为空
     */
    public Integer updateAllContent(){
        //获得所有文章信息
        List<ArticleInfo>  articleInfos=selectListAll();
        int len= MyObjectUtil.iterableCount(articleInfos);
        if(len==0){
            return 0;
        }
        //需要修改的文章集合
        List<ArticleInfo> updList=new ArrayList<>();
        //下载网络图片存储地址
        String folder=myWebMvcConfigurer.getSON_PATH()+"articleImg/";
        String path=myWebMvcConfigurer.getROOT_PATH()+folder;
        String urlPrefix="http://"+myWebMvcConfigurer.getIp()+":"+myWebMvcConfigurer.getPort()+folder;
        //匹配正则
        String pattern="http://[^\" ]+";
        String[] imgSuffixS={".jpg",".png",".gif",".bmp",".jpeg"};//图片url后缀

        for (int i=0;i<len;i++){
            ArticleInfo item=articleInfos.get(i);
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
                updList.add(new ArticleInfo(id,newContent));
            }
        }
        Integer num=updList.size();

        //修改数据库
        batchUpdateSelectiveByList(updList);
        return num;
    }



    public static String getTargetSys(String sysType) {
        if (sysType.equals("公益性系统")) {
            return "gyx_system";
        } else if (sysType.equals("电子商务系统")) {
            return "dzsw_system";
        } else if (sysType.equals("追溯信息系统")) {
            return "zsxx_system";
        } else {
            return null;
        }
    }

    @Resource
    ArticleInfoMapper articleInfoMapper;

    @Resource
    ArticleSystemInfoMapper articleSystemInfoMapper;

    @Resource
    private EsArticleService esArticleService;

    public ObjectRestResponse<JSONObject> listArticle(JSONObject jsonObject) {
        try {
            CommonUtil.fillPageParam(jsonObject);
            if (jsonObject.containsKey("title")) {
                String title = "%" + jsonObject.getString("title") + "%";
                jsonObject.put("title", title);
            }
            int count = articleInfoMapper.countArticle(jsonObject);
            List<JSONObject> list = articleInfoMapper.listArticle(jsonObject);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, count), true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

    public ObjectRestResponse<JSONObject> listArticleDetail(JSONObject jsonObject) {
        try {
            CommonUtil.fillPageParam(jsonObject);
            if (jsonObject.containsKey("title")) {
                String title = "%" + jsonObject.getString("title") + "%";
                jsonObject.put("title", title);
            }
            int count = articleInfoMapper.countArticle(jsonObject);
            List<JSONObject> list = articleInfoMapper.listArticleDetail(jsonObject);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, count), true);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }








    public ObjectRestResponse<JSONObject> delStatusArticle(Integer id, String sysType) {
        Example example = new Example(ArticleSystemInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("articleId", id);
        if (sysType != null) {
            criteria.andEqualTo("systemId", sysType);
        }
        ArticleSystemInfo articleSystemInfo = new ArticleSystemInfo();
        articleSystemInfo.setIsDeleted(1);

        if (sysType != null) {
            delFromEs(sysType, id);
        } else {
            for (String delSysType : allSysTypes) {
                delFromEs(delSysType, id);
            }
        }

        int i = articleSystemInfoMapper.updateByExampleSelective(articleSystemInfo, example);
        if (i > 0) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        } else {
            return new ObjectRestResponse<>(StatusCode.FILEDELETEFAIL);
        }
    }

    public ObjectRestResponse<JSONObject> delRealArticle(int id) {
        Integer i = articleInfoMapper.delRealArticle(id);

        for (String sysType : allSysTypes) {
            delFromEs(sysType, id);
        }

        if (i > 0) {
            return new ObjectRestResponse<>(StatusCode.FILEDELETESUCCESS, true);
        } else {
            return new ObjectRestResponse<>(StatusCode.FILENULL, false);
        }
    }

    /*gyx_system zsxx_system dzsw_system*/
    public ObjectRestResponse<JSONObject> delRealArticleBySys(int id, String sysType) {
        //删除中间表
        Integer i = articleInfoMapper.delRealArticleBySys(id, sysType);

        delFromEs(sysType, id);

        if (i > 0) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL);

    }

    private void delFromEs(String sysType, Integer id) {
        String targetSys = getTargetSys(sysType);
        if (null == targetSys) {
            return;
        }
        EsArticleInfo info = new EsArticleInfo(key);
        info.setId(id);

        DeleteResponse response = null;
        response = esArticleService.delOne(info.getDocId(), targetSys);
    }

    public JSONObject saveImgInfo(int id, String sysType) {
        Integer i = articleInfoMapper.delRealArticleBySys(id, sysType);
//        return new JSONObject(StatusCode.FILEDELETESUCCESS,true);
        return null;
    }

   /* public int addNewArticle(JSONObject jsonObject) {
        //处理文章摘要
        if (jsonObject.getString("content") == null || "".equals(jsonObject.getString("content"))) {
            //直接截取
            String stripHtml = stripHtml(jsonObject.getString("content"));
            jsonObject.put("",stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        jsonObject.put("crtTime",timestamp);
        Integer integer = articleInfoMapper.addNewArticle(jsonObject);

        return integer;
    }*/

    @Transactional(rollbackFor = Exception.class)
    public ObjectRestResponse<T> addNewArticle(ArticleAndSystem article) {
        //处理文章摘要
         /*if (article.getSummary() == null || "".equals(article.getSummary())) {
             //直接截取
             String stripHtml = stripHtml(article.getHtmlContent());
             article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
         }*/
        //新增文章
        article.setPageviews("0");
        //设置创建时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        article.setCrtTime(timestamp);
        Integer integer = articleInfoMapper.addNewArticle(article);
        if (integer < 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
        //List<ArticleSystemInfo> SystemInfoList = new ArrayList<>();
        //批量新增文章_系统关联表
        int insert = 0;
        for (String sysId : article.getSystemId()) {
            //实体
            ArticleSystemInfo articleSystem = new ArticleSystemInfo();
            articleSystem.setArticleId(article.getId());
            articleSystem.setSystemId(sysId);
            articleSystem.setArticleTypeid(article.getArticleTypeid());
            articleSystem.setIsDeleted(0);
            //发布状态
            articleSystem.setStatus(article.getArticleStatus());
            //变化数量
            insert += articleSystemInfoMapper.insert(articleSystem);

            //搜索服务新增文章  0未发布，则不进搜索服务
            if (article.getArticleStatus() == 1) {
                addToEs(sysId, article);
            }

        }
        if (insert <= 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        //批量添加失败，通用mapper继承MySqlMapper未被扫描;未解决
        //int i = articleSystemInfoMapper.insertList(SystemInfoList);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    private void addToEs(String sysType, ArticleAndSystem article) {
        String targetSys = getTargetSys(sysType);
        if (null == targetSys) {
            return;
        }
        EsArticleInfo info = new EsArticleInfo(key, article);
        //新增
        esArticleService.addOne(targetSys, info);
    }

    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    public ObjectRestResponse<Map<String, Object>> getArticleById(int id) {
        //查询文章表
        ArticleInfo articleInfo = selectById(id);
        if (articleInfo == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        // 给当前文章浏览量新增1
        if (articleInfo.getPageviews() == null) {
            articleInfo.setPageviews("1");
        } else {
            int reading = Integer.parseInt(articleInfo.getPageviews()) + 1;
            articleInfo.setPageviews(String.valueOf(reading));
        }
        updateById(articleInfo);
        //查询中间表 系统类型和 文章类型
        Example example = new Example(ArticleSystemInfo.class);
        example.createCriteria().andEqualTo("articleId", articleInfo.getId());
        List<ArticleSystemInfo> articleSysList = articleSystemInfoMapper.selectByExample(example);

        ArticleAndSystem articleAndSystem = new ArticleAndSystem();
//        BeanUtil.copyProperties(articleAndSystem,articleInfo);
        /*for (ArticleSystemInfo articleSys:articleSysList) {
            articleSys.getSystemId();
        }*/
        //测试bean转map
        Map<String, Object> map = BeanUtil.beanToMap(articleInfo);
        map.put("articleSysList", articleSysList);
//        articleAndSystem.setSystemId();

        return new ObjectRestResponse<>(StatusCode.SUCCESS, map, true);
    }

    public ObjectRestResponse<T> updateArticle(ArticleAndSystem andSystem) {
        ArticleInfo article = new ArticleInfo();
        BeanUtil.copyProperties(andSystem, article);

        //修改文章
        article.setUptTime(new Date());
        int i = updateSelectiveById(article);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }

        List<String> sysTypes = Arrays.asList(andSystem.getSystemId());

        //批量修改中间表信息
        ArticleSystemInfo obj = new ArticleSystemInfo();
        //文章ID
        obj.setArticleId(andSystem.getId());
        //文章类型
        obj.setArticleTypeid(andSystem.getArticleTypeid());

        //删除原中间表数据
        for (String sysType : sysTypes) {
            //系统类型
            obj.setSystemId(sysType);
            articleSystemInfoMapper.delete(obj);
        }
        //   articleSystemInfoMapper.delete(obj);  这种删除无法满足单个系统修改
        //发布状态
        obj.setStatus(andSystem.getArticleStatus());
        //新增
        for (String sysType : sysTypes) {
            //重新设置系统类型新增
            obj.setSystemId(sysType);
            articleSystemInfoMapper.insertSelective(obj);
        }
        //插入es服务器中
        if (null == andSystem.getArticleStatus() || andSystem.getArticleStatus() == 0) {
            for (String delSysType : allSysTypes) {
                delFromEs(delSysType, andSystem.getId());
            }
        } else {
            for (String delSysType : allSysTypes) {
                if (sysTypes.contains(delSysType)) {
                    //updateToEs会报错。可能是不存在数据无法修改，会出现Validation Failed: 1: script or doc is missing;2: doc must be specified if doc_as_upsert is enabled
                    //  updateToEs(delSysType, andSystem);
                    delFromEs(delSysType, andSystem.getId());
                    addToEs(delSysType, andSystem);
                } else {
                    delFromEs(delSysType, andSystem.getId());
                }
            }
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    private void updateToEs(String sysType, ArticleAndSystem article) {
        String targetSys = getTargetSys(sysType);
        if (null == targetSys) {
            return;
        }
        EsArticleInfo info = new EsArticleInfo(key, article);
        //修改
        esArticleService.updateOne(targetSys, info);
    }

}
