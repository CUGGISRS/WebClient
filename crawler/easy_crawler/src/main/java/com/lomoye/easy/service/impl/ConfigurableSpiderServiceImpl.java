package com.lomoye.easy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.lomoye.easy.constants.CoreConstant;
import com.lomoye.easy.dao.ConfigurableSpiderMapper;
import com.lomoye.easy.domain.ConfigurableSpider;
import com.lomoye.easy.service.ConfigurableSpiderService;
import com.lomoye.easy.utils.LocalDateUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 2019/8/30 15:16
 * yechangjun
 */
@Slf4j
@Service
public class ConfigurableSpiderServiceImpl extends ServiceImpl<ConfigurableSpiderMapper, ConfigurableSpider> implements ConfigurableSpiderService {

    private static final String COMMON_FIELD = "job_uuid";

    private static final String AUTHOR_FIELD = "author";

    @Autowired
    private HikariDataSource dataSource;

    /**
     * 保存爬取的数据
     */
    @Override
    public void saveData(List<LinkedHashMap<String, String>> datas, ConfigurableSpider spider, String uuid) {
        log.info("start saveData");
        if (datas == null || datas.isEmpty()) {
            log.info("datas empty");
            return;
        }
        //判断表是否存在
        try {
            doSaveData(datas, spider, uuid);
        } catch (Exception e) {
            log.error("saveData error", e);
        }

    }

    @Override
    public LinkedHashMap<String, String> parseFields(String fieldsJson) {
        if (Strings.isNullOrEmpty(fieldsJson)) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONArray fields = JSON.parseArray(fieldsJson);
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            map.put(field.get("key").toString(), field.get("value").toString());
        }
        return map;
    }

    //根据动态表名获取表数据
    @Override
    public List<JSONPObject> getDataById(String tableName) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            //处理为sql
            StringBuilder fieldsStr = new StringBuilder();
            fieldsStr.append("SELECT * FROM ");
            fieldsStr.append(tableName);

            ResultSet resultSet = statement.executeQuery(String.valueOf(fieldsStr));
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                //System.out.println(title);
            }
            //System.out.println(resultSet);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    private void doSaveData(List<LinkedHashMap<String, String>> datas, ConfigurableSpider metaInfo, String uuid) throws SQLException, ClassNotFoundException {
        //处理json存储的表字段
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.putAll(metaInfo.getFields());
        if (metaInfo.getContentFields() != null) {
            fields.putAll(metaInfo.getContentFields());
        }

        //测试发布日期
//        System.out.println(datas.get(0).get("time"));

        //处理动态字段为sql语句
        String sql = getSql(datas, metaInfo, fields, uuid);

        log.info("doSaveData sql={}", sql);

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(sql);
        } catch (Exception e) {
            log.error("doSaveData error", e);
            try {
                //可能是因为表不存在 创建表试试
                createTable(metaInfo.getTableName(), statement, fields);
            } catch (Exception ex) {
                //其他线程可能已经创建了这个表
                log.error("doSaveData createTable error", ex);
            }
            //再执行一遍
            statement.execute(sql);
        } finally {
            statement.close();
            connection.close();
        }


    }

    /**
     * CREATE TABLE job
     * (
     * uuid CHAR(32) NOT NULL COMMENT '主键uuid',
     * name VARCHAR(30) NULL DEFAULT NULL COMMENT '任务名',
     * PRIMARY KEY (uuid)
     * );
     *
     * @param tableName
     * @param statement
     * @param fields    字段
     */
    private void createTable(String tableName, Statement statement, LinkedHashMap<String, String> fields) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "\n");
        StringBuilder fieldDefs = new StringBuilder("( ");
        fieldDefs.append("`").append("id").append("`").append(" BIGINT(20) NOT NULL AUTO_INCREMENT ").append(",\n");
        fields.forEach((k, v) -> {
            StringBuilder fieldDef = new StringBuilder();
            //fieldDef.append("`").append(k).append("`").append(" VARCHAR(255) ").append(",\n");
            //修改-----------------------------start---------------------------------
            //如果是"content"或者"simple"，字段类型设置为text，存储文章
            if (k.equals("content") || k.equals("simple")) {
                fieldDef.append("`").append(k).append("`").append(" TEXT ").append(",\n");
            } else if (k.equals("time")) {
                fieldDef.append("`").append(k).append("`").append(" DATETIME ").append(",\n");
            } else {
                fieldDef.append("`").append(k).append("`").append(" VARCHAR(255) ").append(",\n");
            }
            //修改------------------------------end----------------------------------

            fieldDefs.append(fieldDef);
        });
        fieldDefs.append("`").append(COMMON_FIELD).append("`").append(" CHAR(64) ").append(",\n");

        fieldDefs.append("`").append(AUTHOR_FIELD).append("`").append(" CHAR(64) ").append(",\n");

        fieldDefs.append("`").append("create_time").append("`").append(" DATETIME ").append(",\n");
        fieldDefs.append("`").append("modify_time").append("`").append(" DATETIME ").append(",\n");
        fieldDefs.append("UNIQUE INDEX `unique_page_url` (`").append(CoreConstant.PAGE_URL).append("`),\n");
        fieldDefs.append("PRIMARY KEY (id) \n");
        fieldDefs.append(")");

        sql.append(fieldDefs);
        log.info("createTable sql={}", sql);
        statement.executeUpdate(sql.toString());
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        // 创建连接
        return dataSource.getConnection();
    }

    private String getSql(List<LinkedHashMap<String, String>> datas, ConfigurableSpider metaInfo, LinkedHashMap<String, String> fields, String uuid) {
        StringBuilder fieldsStr = new StringBuilder();
        fields.forEach((k, v) -> {
            fieldsStr.append("`").append(k).append("`").append(",");
        });
        //任务id字段 uuid
        fieldsStr.append("`").append(COMMON_FIELD).append("`");
        //作者新增字段
        fieldsStr.append(",`").append(AUTHOR_FIELD).append("`");
        fieldsStr.append(",`").append("create_time").append("`");
        fieldsStr.append(",`").append("modify_time").append("`");

        StringBuilder sql = new StringBuilder("INSERT INTO `" + metaInfo.getTableName() + "` (" + fieldsStr + ") VALUES ");

        StringBuilder values = new StringBuilder();
        datas.forEach((data) -> {
            StringBuilder sqlValue = new StringBuilder();
            data.forEach((k, v) -> {
                sqlValue.append("'").append(StringEscapeUtils.escapeSql(v)).append("'").append(",");
            });
            sqlValue.append("'").append(uuid).append("'");
            //作者
            sqlValue.append(",'").append(metaInfo.getAuthor()).append("'");
            sqlValue.append(",'").append(LocalDateUtil.getDateTimeAsString(LocalDateTime.now())).append("'");
            sqlValue.append(",'").append(LocalDateUtil.getDateTimeAsString(LocalDateTime.now())).append("'");

            values.append(",(").append(sqlValue).append(" )");
        });

        sql.append(values.substring(1, values.length()));

        return sql.toString();
    }
}
