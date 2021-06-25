package com.github.wxiaoqi.security.info.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.info.sys.entity.ArticleInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ArticleInfoMapper extends CommonMapper<ArticleInfo> {

    List<JSONObject> listArticle(JSONObject jsonObject);

    List<JSONObject> listArticleDetail(JSONObject jsonObject);

    int countArticle(JSONObject jsonObject);

    Integer delRealArticle(int id);

    Integer delRealArticleBySys(int id, String sysType);

    Integer addNewArticle(ArticleInfo articleInfo);

}
