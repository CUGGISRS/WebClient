package com.github.wxiaoqi.security.news.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.news.entity.News;
import com.github.wxiaoqi.security.news.mapper.NewsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class NewsBiz extends BaseBiz<NewsMapper, News> {

    /**
     *
     * @param query
     * @param type
     * @return
     */
    public TableResultResponse<News> selectSimpleByQuery(Query query, String type) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<News> list = null;
        if (type == null || type.isEmpty()) {
            list = mapper.selectSimple();
        } else {
            list = mapper.selectSimpleByType(type);
        }
        return new TableResultResponse<News>(result.getTotal(), list);
    }

}
