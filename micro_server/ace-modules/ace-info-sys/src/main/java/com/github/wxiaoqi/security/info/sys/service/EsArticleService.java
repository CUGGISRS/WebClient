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

    //????????????
//    @Value("${zdlc.system.source_field}")
//    private String source_field;

    @Resource
    ArticleInfoMapper articleInfoMapper;

    @Resource
    RestHighLevelClient restHighLevelClient;

    //????????????
    public ObjectRestResponse<JSONObject> list(int page, int size, ArticleSearchParam articleSearchParam) {
        if (articleSearchParam == null) {
            articleSearchParam = new ArticleSearchParam();
        }
        //????????????????????????
        SearchRequest searchRequest = new SearchRequest(articleSearchParam.getSystem());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //???????????????
        //String[] source_field_array = source_field.split(",");
        //searchSourceBuilder.fetchSource(source_field_array,new String[]{});
        //????????????????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //????????????
        //?????????????????????
        if (StringUtils.isNotEmpty(articleSearchParam.getKeyword())) {
            //???????????????
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(articleSearchParam.getKeyword(), "title", "content", "simple");
            //??????????????????
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //?????????????????????boost???
            multiMatchQueryBuilder.field("title", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        /*if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            //??????????????????
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        }*/
        //??????boolQueryBuilder???searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        //??????????????????
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        //??????????????????
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //????????????
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //??????????????????
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

//        QueryResult<ArticleInfo> queryResult = new QueryResult();
        JSONObject queryResult = new JSONObject();
        List<ArticleInfo> list = new ArrayList<>();
        try {
            //????????????
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //??????????????????
            SearchHits hits = searchResponse.getHits();
            //?????????????????????
//            long totalHits = hits.totalHits;
            TotalHits totalHits=hits.getTotalHits();
            long total = totalHits==null?0:totalHits.value;

            queryResult.put("total", total);
//            queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            if(searchHits!=null){
                for (SearchHit hit : searchHits) {
                    ArticleInfo articleInfo = new ArticleInfo();
                    //?????????
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    //??????id
//                String id = Integer.toString((Integer) sourceAsMap.get("id"));
//                String id = (String)sourceAsMap.get("id");
//                String id = sourceAsMap.get("id").toString();
                    articleInfo.setId((Integer) sourceAsMap.get("id"));
                    //??????name
                    String name = (String) sourceAsMap.get("title");
                    //??????????????????name
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
                    //??????
                    articleInfo.setTitle(name);
                    //????????????
//                String content = (String) sourceAsMap.get("content");
//                articleInfo.setContent(content);
                    //????????????
                    String pub_time = (String) sourceAsMap.get("pub_time");
                    if(pub_time!=null){
                        pub_time = pub_time.replace("Z", " UTC");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                        Date d = format.parse(pub_time);
                        articleInfo.setPubTime(d);
                    }

                    //??????
                    String simple = (String) sourceAsMap.get("simple");
                    articleInfo.setSimple(simple);
                    //???coursePub????????????list
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

    //????????????
    public DeleteResponse delOne(String docId, String targetSys) {
        // type?????????
        DeleteRequest deleteRequest = new DeleteRequest(targetSys, docId);
        DeleteResponse response = null;
        try {
            response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //??????
    public IndexResponse addOne(String targetSys, EsArticleInfo esArticleInfo) {
        IndexRequest indexRequest = new IndexRequest(targetSys).id(esArticleInfo.getDocId());
        indexRequest.source(esArticleInfo.getDoc());
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//            System.out.println("????????????" + indexResponse.toString());
        } catch (ElasticsearchException e) {
            System.out.println("????????????????????????" + e.getDetailedMessage());
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ElasticsearchException(e.getMessage());
        }
        return indexResponse;
    }

    //??????
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
     * ??????????????????
     *
     * @return ???????????????true???200?????????????????????????????????500
     */
    public ObjectRestResponse<JSONObject> refreshAll() {
        String message = "";
        try {
            for (String sysType : ArticleInfoBiz.allSysTypes) {
                String index = ArticleInfoBiz.getTargetSys(sysType);
                if (index == null) {
                    continue;
                }
                //???????????????????????????????????????????????????????????????
                DeleteIndexRequest deleteRequest = new DeleteIndexRequest(index);
                try {
                    //????????????
                    AcknowledgedResponse deleteIndexResponse = restHighLevelClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
                    //????????????????????????????????????
                    if (!deleteIndexResponse.isAcknowledged()) {
                        message = index + "??????????????????";
                        break;
                    }
                } catch (ElasticsearchStatusException e) {
                    System.out.println(index + "?????????????????????" + e.getMessage());
                }
                //??????????????????
                CreateIndexRequest request = new CreateIndexRequest(index);
                //????????????????????????
                request.settings(Settings.builder()
                        .put("index.number_of_shards", 5)
                        .put("index.number_of_replicas", 1)
                );
                //???????????????????????????????????????ES??????JavaDTO???????????????JSON?????????
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
                //????????????
                CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
                //??????????????????????????????
                if (!createIndexResponse.isAcknowledged()) {
                    message = index + "??????????????????";
                    break;
                }
                // ??????????????????elastic
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
                            System.out.println(article.getString("id") + "?????????????????????" + e.getMessage());

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????:" + e.getMessage();
        }
        if ("".equals(message)) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successJson(), true);
        } else {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.errorJson(StatusCode.FAIL, message), true);
        }
    }
}
