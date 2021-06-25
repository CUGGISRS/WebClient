package com.github.wxiaoqi.security.info.sys.entity;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EsArticleInfo {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public EsArticleInfo(String key) {
        this.key = key;
    }

    public EsArticleInfo(String key, Integer id) {
        this.key = key;
        this.id = id;
    }

    public EsArticleInfo(String key, ArticleAndSystem article) {
        this.key = key;
        //文章id
        this.setId(article.getId());
        //文章类型
        this.setArticle_typeid(article.getArticleTypeid());
        //文字标题
        this.setTitle(article.getTitle());
        //文章内容
        this.setContent(article.getContent());
        //文章简介
        this.setSimple(article.getSimple());
        //发布时间
        this.setPub_time(article.getPubTime());
        //作者
        this.setAuthor(article.getAuthor());
    }

    public EsArticleInfo(String key, Map<String, Object> map) {
        this.key = key;
        //文章id
        if (map.containsKey("id")) {
            this.setId(Integer.parseInt(map.get("id").toString()));
        }
        //文章类型
        if (map.containsKey("article_typeid")) {
            this.setArticle_typeid(map.get("article_typeid").toString());
        }
        //文字标题
        if (map.containsKey("title")) {
            this.setTitle(map.get("title").toString());
        }
        //文章内容
        if (map.containsKey("content")) {
            this.setContent(map.get("content").toString());
        }
        //文章简介
        if (map.containsKey("simple")) {
            this.setSimple(map.get("simple").toString());
        }
        //发布时间
        if (map.containsKey("pub_time")) {
            try {
                this.setPub_time(dateFormat.parse(map.get("pub_time").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //作者
        if (map.containsKey("author")) {
            this.setAuthor(map.get("author").toString());
        }
    }

    public EsArticleInfo(String key, JSONObject jsonObject) {
        this(key, convertJsonToMap(jsonObject));
    }

    public static Map<String, Object> convertJsonToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 获得elastic文档Id
     *
     * @return elastic文档Id
     */
    public String getDocId() {
        return key + id.toString();
    }

    /**
     * 获得elastic文档键值对
     *
     * @return elastic文档键值对
     */
    public Map<String, Object> getDoc() {
        Map<String, Object> doc = new HashMap<>();
        //文章关键标记
        doc.put("key", key);
        //文章id
        doc.put("id", id);
        //文章类型
        doc.put("article_typeid", article_typeid);
        //文字标题
        doc.put("title", title);
        //文章内容
        doc.put("content", content);
        //文章简介
        doc.put("simple", simple);
        //发布时间
//        doc.put("pub_time", dateFormat.format(pub_time));
        doc.put("pub_time", pub_time);
        //作者
        doc.put("author", author);
        Map<String, Object> map = MapUtil.removeNullValue(doc);
        return map;
    }

    private String key;
    private Integer id;
    private String article_typeid;
    private String title;
    private String author;
    private String simple;
    private String content;
    private Date pub_time;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticle_typeid() {
        return article_typeid;
    }

    public void setArticle_typeid(String article_typeid) {
        this.article_typeid = article_typeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSimple() {
        return simple;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPub_time() {
        return pub_time;
    }

    public void setPub_time(Date pub_time) {
        this.pub_time = pub_time;
    }
}
