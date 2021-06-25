package com.github.wxiaoqi.security.info.sys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.biz.ArticleInfoBiz;
import com.github.wxiaoqi.security.info.sys.entity.ArticleInfo;
import com.github.wxiaoqi.security.info.sys.entity.ArticleSystemInfo;
import com.github.wxiaoqi.security.info.sys.entity.EsArticleInfo;
import com.github.wxiaoqi.security.info.sys.mapper.ArticleInfoMapper;
import com.github.wxiaoqi.security.info.sys.mapper.ArticleSystemInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.info.sys.vo.ArticleSearchParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Console;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class EsArticleService {

    @Value("${zdlc.elasticsearch.key_Article}")
    private String key;

    @Value("${zdlc.elasticsearch.pageRow}")
    private Integer pageRow;

    //过滤字段
//    @Value("${zdlc.system.source_field}")
//    private String source_field;

    @Resource
    ArticleInfoMapper articleInfoMapper;

    @Resource
    RestHighLevelClient restHighLevelClient;

    //文章搜索
    public ObjectRestResponse<JSONObject> list(int page, int size, ArticleSearchParam articleSearchParam) {
        if (articleSearchParam == null) {
            articleSearchParam = new ArticleSearchParam();
        }
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(articleSearchParam.getSystem());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过虑源字段
        //String[] source_field_array = source_field.split(",");
        //searchSourceBuilder.fetchSource(source_field_array,new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(articleSearchParam.getKeyword())) {
            //匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(articleSearchParam.getKeyword(), "title", "content", "simple");
            //设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升单个字段的boost值
            multiMatchQueryBuilder.field("title", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        /*if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            //根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        }*/
        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        //设置分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        //起始记录下标
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

//        QueryResult<ArticleInfo> queryResult = new QueryResult();
        JSONObject queryResult = new JSONObject();
        List<ArticleInfo> list = new ArrayList<>();
        try {
            //执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
//            long totalHits = hits.totalHits;
            TotalHits totalHits=hits.getTotalHits();
            long total = totalHits==null?0:totalHits.value;

            queryResult.put("total", total);
//            queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            if(searchHits!=null){
                for (SearchHit hit : searchHits) {
                    ArticleInfo articleInfo = new ArticleInfo();
                    //源文档
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    //取出id
//                String id = Integer.toString((Integer) sourceAsMap.get("id"));
//                String id = (String)sourceAsMap.get("id");
//                String id = sourceAsMap.get("id").toString();
                    articleInfo.setId((Integer) sourceAsMap.get("id"));
                    //取出name
                    String name = (String) sourceAsMap.get("title");
                    //取出高亮字段name
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields != null) {
                        HighlightField highlightFieldName = highlightFields.get("title");
                        if (highlightFieldName != null) {
                            Text[] fragments = highlightFieldName.fragments();
                            StringBuffer stringBuffer = new StringBuffer();
                            for (Text text : fragments) {
                                stringBuffer.append(text);
                            }
                            name = stringBuffer.toString();
                        }
                    }
                    //标题
                    articleInfo.setTitle(name);
                    //文章内容
//                String content = (String) sourceAsMap.get("content");
//                articleInfo.setContent(content);
                    //发布时间
                    String pub_time = (String) sourceAsMap.get("pub_time");
                    if(pub_time!=null){
                        pub_time = pub_time.replace("Z", " UTC");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                        Date d = format.parse(pub_time);
                        articleInfo.setPubTime(d);
                    }

                    //简介
                    String simple = (String) sourceAsMap.get("simple");
                    articleInfo.setSimple(simple);
                    //将coursePub对象放入list
                    list.add(articleInfo);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        queryResult.setList(list);
        queryResult.put("rows", list);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, queryResult);
    }

    //文章删除
    public DeleteResponse delOne(String docId, String targetSys) {
        // type固定值
        DeleteRequest deleteRequest = new DeleteRequest(targetSys, docId);
        DeleteResponse response = null;
        try {
            response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //新增
    public IndexResponse addOne(String targetSys, EsArticleInfo esArticleInfo) {
        IndexRequest indexRequest = new IndexRequest(targetSys).id(esArticleInfo.getDocId());
        indexRequest.source(esArticleInfo.getDoc());
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//            System.out.println("新增成功" + indexResponse.toString());
        } catch (ElasticsearchException e) {
            System.out.println("写入索引产生冲突" + e.getDetailedMessage());
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ElasticsearchException(e.getMessage());
        }
        return indexResponse;
    }

    //修改
    public UpdateResponse updateOne(String targetSys, EsArticleInfo esArticleInfo) {
        UpdateRequest request = new UpdateRequest(targetSys, esArticleInfo.getDocId());
        request.docAsUpsert(true);
        UpdateResponse updateResponse = null;
        try {
            updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            long version = updateResponse.getVersion();
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("insert success, version is " + version);
            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("update success, version is " + version);
            } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {

            } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return updateResponse;
    }

    /**
     * 刷新所有数据
     *
     * @return 成功则返回true，200；失败则返回错误信息，500
     */
    public ObjectRestResponse<JSONObject> refreshAll() {
        String message = "";
        try {
            for (String sysType : ArticleInfoBiz.allSysTypes) {
                String index = ArticleInfoBiz.getTargetSys(sysType);
                if (index == null) {
                    continue;
                }
                //删除索引请求，如果创建之前存在就需要删除掉
                DeleteIndexRequest deleteRequest = new DeleteIndexRequest(index);
                try {
                    //删除索引
                    AcknowledgedResponse deleteIndexResponse = restHighLevelClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
                    //如果没删除成功，抛出异常
                    if (!deleteIndexResponse.isAcknowledged()) {
                        message = index + "索引删除失败";
                        break;
                    }
                } catch (ElasticsearchStatusException e) {
                    System.out.println(index + "索引删除出错：" + e.getMessage());
                }
                //创建索引请求
                CreateIndexRequest request = new CreateIndexRequest(index);
                //设置分片和副本数
                request.settings(Settings.builder()
                        .put("index.number_of_shards", 5)
                        .put("index.number_of_replicas", 1)
                );
                //创建映射，中间填写需要存到ES上的JavaDTO对象对应的JSON数据。
                String strMappings = "{\n" +
                        "\t\"properties\": {\n" +
                        "\t\t\"@timestamp\": {\n" +
                        "\t\t\t\"type\": \"date\"\n" +
                        "\t\t},\n" +
                        "\t\t\"@version\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"title\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"search_analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"article_typeid\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"author\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"search_analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"pub_time\": {\n" +
                        "\t\t\t\"type\": \"date\"\n" +
                        "\t\t},\n" +
                        "\t\t\"simple\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"search_analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"content\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"search_analyzer\": \"ik_max_word\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"key\": {\n" +
                        "\t\t\t\"type\": \"text\",\n" +
                        "\t\t\t\"fields\": {\n" +
                        "\t\t\t\t\"keyword\": {\n" +
                        "\t\t\t\t\t\"ignore_above\": 256,\n" +
                        "\t\t\t\t\t\"type\": \"keyword\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"id\": {\n" +
                        "\t\t\t\"type\": \"long\"\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}";
                request.mapping(strMappings, XContentType.JSON);
                //创建索引
                CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
                //如果没成功，抛出异常
                if (!createIndexResponse.isAcknowledged()) {
                    message = index + "索引创建失败";
                    break;
                }
                // 获取文章插入elastic
                JSONObject cond = new JSONObject();
                cond.put("sysType", sysType);
                cond.put("status", "1");
                int count = articleInfoMapper.countArticle(cond);
                int pageCount = new Double(Math.ceil((double) count / pageRow)).intValue();
                for (int page = 0; page < pageCount; page++) {
                    int offSet = page * pageRow;
                    cond.put("offSet", offSet);
                    cond.put("pageRow", pageRow);
                    List<JSONObject> articleInfos = articleInfoMapper.listArticleDetail(cond);
                    for (JSONObject article : articleInfos) {
                        try {
                            addOne(index, new EsArticleInfo(key, article));
                        } catch (Exception e) {
                            System.out.println(article.getString("id") + "文章索引出错：" + e.getMessage());

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "索引刷新失败:" + e.getMessage();
        }
        if ("".equals(message)) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successJson(), true);
        } else {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.errorJson(StatusCode.FAIL, message), true);
        }
    }
}
