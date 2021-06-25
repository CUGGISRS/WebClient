package com.zd.earth.manage.common;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zd.earth.manage.constants.Constants;
import com.zd.earth.manage.msg.TableResultResponse;
import com.zd.earth.manage.util.MyObjectUtil;
import com.zd.earth.manage.vo.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.AG
 * Date: 17/1/13
 * Time: 15:13
 * Version 1.0.0
 */
public abstract class BaseBiz<M extends CommonMapper<T>, T> {
    @Autowired
    protected M mapper;
    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }


    public T selectById(Object id) {
        if(id==null){
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return new Long(mapper.selectCount(entity));
    }


    public int insert(T entity) {

        return mapper.insert(entity);
    }



    public int insertSelective(T entity) {

        return mapper.insertSelective(entity);
    }


    public int delete(T entity) {
        return mapper.delete(entity);
    }


    public int deleteById(Object id) {
        if(id==null){
            return 0;
        }
        return mapper.deleteByPrimaryKey(id);
    }


    public int updateSelectiveById(T entity) {

        return mapper.updateByPrimaryKeySelective(entity);
    }
    public int updateById(T entity) {

        return mapper.updateByPrimaryKey(entity);
    }





    /**
     * 通过等于集合查询数据
     */
    public List<T> getByToMap( Map<String,Object> toMap){
        return getMayCondition(null,null,null,null,null,null,
                toMap,null,null,null,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null,null,
                null,null);
    }


    /**
     * 通过等于集合删除数据
     */
    public int delByToMap( Map<String,Object> toMap){
        return delMayCondition(null,null,null,null,
                toMap,null,null,null,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null,null,
                null,null);
    }

    /**
     * 通过一属性的属性名和多个属性值(加上等于集合)获取数据
     */
    public <K> List<T> getByInFiledMayToMap(List<K> propertyValue,String propertyName,Map<String ,Object> toMap){

        return   getByInFiledMayCondition(propertyValue,propertyName,null,null,null,
                null,null,null,
                toMap,null,null,null,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null,null,
                null,null);
    }

    /**
     * 通过一属性的属性名和多个属性值(加上等于集合)删除数据
     */
    public <K> int delByInFiledMayToMap(List<K> propertyValue,String propertyName,Map<String ,Object> toMap){

        return  delByInFiledMayCondition(propertyValue,propertyName,null,null,null,null,
                toMap,null,null,null,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null,null,
                null,null);
    }


    /**
     * 通过属性名为key和属性值为value的map集合修改数据
     */
    public int updateByFiledMap(T t,Map<String,Object> toMap){
        return updByMayCondition(t,null,null,null,null,toMap,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null);
    }

    /**
     * 通过一属性的属性名和多个属性值(加上等于集合)修改数据
     */
    public <K> int updateByInFiledMayToMap(T t,List<K> propertyValue,String propertyName,Map<String,Object> toMap){
        return updByInFiledMayCondition(t,propertyValue,propertyName,null,null,null,null,
                toMap,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null);
    }


    /**
     * 通过id集合进行批量删除
     * @param ids
     * @return
     */
    public int batchDeleteByIds(List<Integer> ids){
        if(isUselessList(ids)){
            return 0;
        }
        int size=ids.size();
        int limit=500;
        if(size>limit){
            int sum=0;
            int index=(int) Math.ceil((double) size/limit);
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<Integer> blockList=ids.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                String blockListStr= StringUtils.join(blockList,",");
                int blockIndex=mapper.deleteByIds(blockListStr);
                sum+=blockIndex;
            }
            return sum;
        }
        String str= StringUtils.join(ids,",");
        return  mapper.deleteByIds(str);
    }



    /**
     * 通过id集合查询数据
     * @param ids
     * @return
     */
    public List<T>  batchSelectByIds(List<Integer> ids){
        if(isUselessList(ids)){
            return null;
        }
        int size=ids.size();
        int limit=500;
        if(size>limit){
            int index=(int) Math.ceil((double) size/limit);
            List<T> ts=new ArrayList<>();
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<Integer> blockList=ids.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                String blockListStr= StringUtils.join(blockList,",");
                List<T> tList=mapper.selectByIds(blockListStr);
                ts.addAll(tList);
            }
            return ts;
        }
        String str= StringUtils.join(ids,",");
        return  mapper.selectByIds(str);
    }

    /**
     *根据主键选择性的批量更新数据库，,属性值为null不会更新
     * @param list
     * @return
     */
    public int batchUpdateSelectiveByList(List<T> list){
        if(isUselessList(list)){
            return 0;
        }
        int size=list.size();
        int limit=500;
        if(size>limit){
            int sum=0;
            int index=(int) Math.ceil((double) size/limit);
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<T> blockList=list.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                int blockIndex=mapper.updateBatchByPrimaryKeySelective(blockList);
                sum+=blockIndex;
            }
            return sum;
        }
        return mapper.updateBatchByPrimaryKeySelective(list);
    }

    /**
     *批量插入,不包括主键，会自动添加自增主键到集合中,属性值为null也会插入
     * @param list
     * @return
     */
    public int batchInsertByList(List<T> list){
        if(isUselessList(list)){
            return 0;
        }
        int size=list.size();
        int limit=500;
        if(size>limit){
            int sum=0;
            int index= (int) Math.ceil((double) size/limit);
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<T> blockList=list.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                int blockIndex=mapper.insertList(blockList);
                sum+=blockIndex;
            }
            return sum;
        }
        return mapper.insertList(list);
    }

    /**
     *批量插入数据库，所有字段都插入，包括主键，不会自动添加自增主键到集合中,属性值为null也会插入
     * @param list
     * @return
     */
    public int batchInsertALLColsByList(List<T> list){
        if(isUselessList(list)){
            return 0;
        }
        int size=list.size();
        int limit=500;
        if(size>limit){
            int sum=0;
            int index=(int) Math.ceil((double) size/limit);
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<T> blockList=list.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                int blockIndex=mapper.insertListUseAllCols(blockList);
                sum+=blockIndex;
            }
            return sum;
        }
        return mapper.insertListUseAllCols(list);
    }




    /**
     * 通过id拼接的字符串获得数据集合
     * @param idsStr
     * @return
     */
    public List<T> getListByIds(String idsStr){
        if(idsStr==null||"".equals(idsStr.trim())){
            return null;
        }
        String[] ids0=idsStr.split(",");
        List<Integer> ids=new ArrayList<>();
        for (int i=0,len=ids0.length;i<len;i++){
            ids.add(Integer.valueOf(ids0[i]));
        }
        return batchSelectByIds(ids);
    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }





    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }


    /**
     * 某一基础条件下追加模糊查询条件
     */
    public TableResultResponse<T> selectByQueryAndFiled(Map<String, Object> params,String fieldName,Object fieldValue) {
        Map<String,Object> toMap=new HashMap<>();
        toMap.put(fieldName,fieldValue);
        return selectByQuery(null,null,params,null,null,null,
                toMap,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }

    public Example getExampleByQuery(Query query) {
        return getExampleByConditions(null,null,query,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null);
    }



    /**
     * 集合按照某一字段的拼音首字母排序
     */
    public  void sortListByPinyin(List<T> list,String getField) {
        // 获取中文环境
        Comparator comparator = Collator.getInstance(Locale.CHINA);
        // 排序实现
        Collections.sort(list, (e1, e2) -> {
            Class<T> clazz =getClassT();
            try {
                Method setObj  = clazz.getMethod(getField);
                return comparator.compare( setObj.invoke(e1),  setObj.invoke(e2));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            //抛出异常则返回相等
            return 0;
        });
    }


    /**
     * 通过某些条件查询
     */
    public List<T> getMayCondition( String[] columns,String orderBy,
                                           Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                           List<String> nullList,List<String> notNullList,
                                           Map<String,Object> toMap,  Map<String,Object> notToMap,
                                           Map<String,Object> gMap,Map<String,Object> gtMap,
                                           Map<String,Object> lMap,Map<String,Object> ltMap,
                                           Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                           Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                           List<String> orNullList,List<String> orNotNullList,
                                           Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                           Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                           Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                           Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        Example example =getExampleByConditions(columns,orderBy,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.selectByExample(example);
    }

    /**
     * 通过某些条件删除
     */
    public int delMayCondition(Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                     List<String> nullList,List<String> notNullList,
                                     Map<String,Object> toMap,  Map<String,Object> notToMap,
                                     Map<String,Object> gMap,Map<String,Object> gtMap,
                                     Map<String,Object> lMap,Map<String,Object> ltMap,
                                     Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                     Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                     List<String> orNullList,List<String> orNotNullList,
                                     Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                     Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                     Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                     Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        Example example =getExampleByConditions(null,null,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.deleteByExample(example);
    }
    /**
     * 通过某些条件修改
     */
    public int updByMayCondition(T t,
                                 Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                 List<String> nullList,List<String> notNullList,
                                 Map<String,Object> toMap,  Map<String,Object> notToMap,
                                 Map<String,Object> gMap,Map<String,Object> gtMap,
                                 Map<String,Object> lMap,Map<String,Object> ltMap,
                                 Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                 Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                 List<String> orNullList,List<String> orNotNullList,
                                 Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                 Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                 Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                 Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        Example example =getExampleByConditions(null,null,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.updateByExampleSelective(t,example);
    }

    /**
     * 通过一属性的属性名和多个属性值(加上其他条件)获取数据
     */
    public <K> List<T> getByInFiledMayCondition(List<K> fieldValue,String fieldName,
                                                     String[] columns,String orderBy,
                                                     Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                                     List<String> nullList,List<String> notNullList,
                                                     Map<String,Object> toMap,  Map<String,Object> notToMap,
                                                     Map<String,Object> gMap,Map<String,Object> gtMap,
                                                     Map<String,Object> lMap,Map<String,Object> ltMap,
                                                     Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                                     Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                                     List<String> orNullList,List<String> orNotNullList,
                                                     Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                                     Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                                     Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                                     Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        if(isUselessKey(fieldName)||isUselessList(fieldValue)){
            return null;
        }
        int size=fieldValue.size();
        int limit=500;
        if(inMap==null){
            inMap=new HashMap<>();
        }
        if(size>limit){
            int index=(int) Math.ceil((double) size/limit);
            List<T> ts=new ArrayList<>();
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<K> blockList=fieldValue.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                inMap.put(fieldName,blockList);
                Example example =getExampleByConditions(columns,orderBy,null,likeMap,notLikeMap,nullList,notNullList,
                        toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                        orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
                List<T> tList=mapper.selectByExample(example);
                ts.addAll(tList);
            }
            return ts;
        }

        inMap.put(fieldName,fieldValue);
        Example example =getExampleByConditions(columns,orderBy,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.selectByExample(example);
    }

    /**
     * 通过一属性的属性名和多个属性值(加上其他条件)删除数据
     */
    public <K> int delByInFiledMayCondition(List<K> filedValue,String filedName,
                                               Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                               List<String> nullList,List<String> notNullList,
                                               Map<String,Object> toMap,  Map<String,Object> notToMap,
                                               Map<String,Object> gMap,Map<String,Object> gtMap,
                                               Map<String,Object> lMap,Map<String,Object> ltMap,
                                               Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                               Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                               List<String> orNullList,List<String> orNotNullList,
                                               Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                               Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                               Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                               Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        if(isUselessKey(filedName)||isUselessList(filedValue)){
            return 0;
        }
        if(inMap==null){
            inMap=new HashMap<>();
        }
        int size=filedValue.size();
        int limit=500;
        if(size>limit){
            int index=(int) Math.ceil((double) size/limit);
            int sum=0;
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<K> blockList=filedValue.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                inMap.put(filedName,blockList);
                Example example =getExampleByConditions(null,null,null,likeMap,notLikeMap,nullList,notNullList,
                        toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                        orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
                int t=mapper.deleteByExample(example);
                sum+=t;
            }
            return sum;
        }
        inMap.put(filedName,filedValue);
        Example example =getExampleByConditions(null,null,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.deleteByExample(example);

    }


    /**
     * 通过一属性的属性名和多个属性值(加上其他条件)修改数据
     */
    public <K> int updByInFiledMayCondition(T obj,List<K> filedValue,String filedName,
                                            Map<String, Object> likeMap,Map<String,Object> notLikeMap,
                                            List<String> nullList,List<String> notNullList,
                                            Map<String,Object> toMap,  Map<String,Object> notToMap,
                                            Map<String,Object> gMap,Map<String,Object> gtMap,
                                            Map<String,Object> lMap,Map<String,Object> ltMap,
                                            Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                            Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                            List<String> orNullList,List<String> orNotNullList,
                                            Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                            Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                            Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                            Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap){
        if(isUselessKey(filedName)||isUselessList(filedValue)){
            return 0;
        }
        int size=filedValue.size();
        int limit=500;
        if(inMap==null){
            inMap=new HashMap<>();
        }
        if(size>limit){
            int index=(int) Math.ceil((double) size/limit);
            int sum=0;
            for (int i=0;i<index;i++){
                //stream流表达式，skip表示跳过前i*500条记录，limit表示读取当前流的前500条记录
                List<K> blockList=filedValue.stream().skip(i*limit).limit(limit).collect(Collectors.toList());
                inMap.put(filedName,blockList);
                int t=mapper.updateByExampleSelective(obj,getExampleByConditions(null,null,null,
                        likeMap,notLikeMap,nullList,notNullList,
                        toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                        orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap));
                sum+=t;
            }
            return sum;
        }
        inMap.put(filedName,filedValue);
        Example example =getExampleByConditions(null,null,null,likeMap,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        return mapper.updateByExampleSelective(obj,example);

    }


    /**
     * 某些查询条件下追加模糊查询来查询条数
     */
    public Integer countByQuery(Map<String, Object> params,Map<String,Object> notLikeMap,
                                                List<String> nullList,List<String> notNullList,
                                                Map<String,Object> toMap,  Map<String,Object> notToMap,
                                                Map<String,Object> gMap,Map<String,Object> gtMap,
                                                Map<String,Object> lMap,Map<String,Object> ltMap,
                                                Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                                Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                                List<String> orNullList,List<String> orNotNullList,
                                                Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
                                                Map<String,Object> orGMap,Map<String,Object> orGtMap,
                                                Map<String,Object> orLMap,Map<String,Object> orLtMap,
                                                Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap) {

        Query query = new Query(params);
        Example example =getExampleByConditions(null,null,query,null,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
       return selectCountByExample(example);
    }


    /**
     * 某些查询条件下追加模糊查询来分页，不传limit参数则不分页(查询指定列)
     */
    public TableResultResponse<T> selectByQuery(
            String[] columns,String orderBy,
            Map<String, Object> params,Map<String,Object> notLikeMap,
            List<String> nullList,List<String> notNullList,
            Map<String,Object> toMap,  Map<String,Object> notToMap,
            Map<String,Object> gMap,Map<String,Object> gtMap,
            Map<String,Object> lMap,Map<String,Object> ltMap,
            Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

            Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
            List<String> orNullList,List<String> orNotNullList,
            Map<String,Object> orToMap,  Map<String,Object> orNotToMap,
            Map<String,Object> orGMap,Map<String,Object> orGtMap,
            Map<String,Object> orLMap,Map<String,Object> orLtMap,
            Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap) {

        Query query = new Query(params);
        Example example =getExampleByConditions(columns,orderBy,query,null,notLikeMap,nullList,notNullList,
                toMap ,notToMap,gMap,gtMap,lMap,ltMap,inMap,notInMap,orLikeMap,orNotLikeMap,orNullList,orNotNullList,
                orToMap,orNotToMap,orGMap,orGtMap,orLMap,orLtMap,orInMap,orNotInMap);
        if(query.getLimit()!=0){
            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<T> list = mapper.selectByExample(example);
            return new TableResultResponse<T>(result.getTotal(), list);
        }
        List<T> list = mapper.selectByExample(example);
        return new TableResultResponse<T>(list.size(), list);
    }
    /**
     * 某些查询条件下追加模糊查询条件
     */
    public <K> Example getExampleByConditions(String[] columns, String orderBy, Query query,
                                              Map<String,Object> likeMap, Map<String,Object> notLikeMap,
                                              List<String> nullList, List<String> notNullList,
                                              Map<String,Object> toMap, Map<String,Object> notToMap,
                                              Map<String,Object> gMap, Map<String,Object> gtMap,
                                              Map<String,Object> lMap, Map<String,Object> ltMap,
                                              Map<String,Iterable> inMap, Map<String,Iterable> notInMap,

                                              Map<String,Object> orLikeMap, Map<String,Object> orNotLikeMap,
                                              List<String> orNullList, List<String> orNotNullList,
                                              Map<String,Object> orToMap, Map<String,Object> orNotToMap,
                                              Map<String,Object> orGMap, Map<String,Object> orGtMap,
                                              Map<String,Object> orLMap, Map<String,Object> orLtMap,
                                              Map<String,Iterable> orInMap, Map<String,Iterable> orNotInMap
                                         ) {
        Class<T> clazz =getClassT();
        Example example = new Example(clazz);
        if(columns!=null){
            //设置需要查询的列
            example.selectProperties(columns);
        }
        Example.Criteria criteria = example.createCriteria();
        int sum=0;
        if(query==null){
            sum+= setLikeMap(clazz,criteria,likeMap);
        }
        sum+= setNotLikeMap(clazz,criteria,notLikeMap,query);
        sum+= setNullList(clazz,criteria,nullList,query);
        sum+= setNotNullList(clazz,criteria,notNullList,query);
        sum+= setToMap(clazz,criteria,toMap,query);
        sum+= setNotToMap(clazz,criteria,notToMap,query);
        sum+=setGMap(clazz,criteria,gMap,query);
        sum+=setGTMap(clazz,criteria,gtMap,query);
        sum+=setLMap(clazz,criteria,lMap,query);
        sum+=setLTMap(clazz,criteria,ltMap,query);
        sum+=setInMap(clazz,criteria,inMap,query);
        sum+=setNotInMap(clazz,criteria,notInMap,query);


        Example.Criteria criteria1 = example.createCriteria();
        int sum1=0;
        sum1+=setOrLikeMap(clazz,criteria1,orLikeMap,query);
        sum1+=setOrNotLikeMap(clazz,criteria1,orNotLikeMap,query);
        sum1+= setOrNullList(clazz,criteria1,orNullList,query);
        sum1+=setOrNotNullList(clazz,criteria1,orNotNullList,query);
        sum1+=setOrToMap(clazz,criteria1,orToMap,query);
        sum1+=setOrNotToMap(clazz,criteria1,orNotToMap,query);
        sum1+=setOrGMap(clazz,criteria1,orGMap,query);
        sum1+=setOrGTMap(clazz,criteria1,orGtMap,query);
        sum1+=setOrLMap(clazz,criteria1,orLMap,query);
        sum1+=setOrLTMap(clazz,criteria1,orLtMap,query);
        sum1+= setOrInMap(clazz,criteria1,orInMap,query);
        sum1+=setOrNotInMap(clazz,criteria1,orNotInMap,query);

        //参数过滤后，模糊查询参数再添加
        sum+=setLikeMap(clazz,criteria,query);
        if(sum1!=0){
            example.and(criteria1);
        }
        if(sum==0&&sum1==0){
            example.clear();
        }

        if(orderBy!=null){
            example.setOrderByClause(orderBy);
        }
        return example;
    }

    /**
     * 设置模糊在查询参数
     */
    private  int setLikeMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> likeParam) {
        int sum=0;
        if (likeParam!=null&&likeParam.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : likeParam.entrySet()) {
                if(isJoinSqlObject(null,clazz,entry)){
                    criteria.andLike(entry.getKey(),"%" + entry.getValue().toString() + "%");
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置模糊不在查询参数
     */
    private  int setNotLikeMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> likeParam, Query query) {
        int sum=0;
        if (likeParam!=null&&likeParam.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : likeParam.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andNotLike(entry.getKey(),"%" + entry.getValue().toString() + "%");
                    sum++;
                }
            }
        }
        return sum;
    }


    /**
     * 设置为null查询参数
     */
    private  int setNullList(Class<T> clazz, Example.Criteria criteria, List<String> notNullList, Query query) {
        int sum=0;
        if (notNullList!=null&&notNullList.size() > 0) {
            for (String key: notNullList) {
                if(isJoinSqlKey(query,key,clazz)){
                    criteria.andIsNull(key);
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置不为null查询参数
     */
    private  int setNotNullList(Class<T> clazz, Example.Criteria criteria, List<String> notNullList, Query query) {
        int sum=0;
        if (notNullList!=null&&notNullList.size() > 0) {
            for (String key: notNullList) {
                if(isJoinSqlKey(query,key,clazz)){
                    criteria.andIsNotNull(key);
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置等于查询参数
     */
    private  int setToMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> toMap, Query query) {
        int sum=0;
        if (toMap!=null&&toMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : toMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * 设置不等于查询参数
     */
    private  int setNotToMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> notToMap, Query query) {
        int sum=0;
        if (notToMap!=null&&notToMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : notToMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andNotEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置大于查询参数
     */
    private  int setGMap(Class<T> clazz, Example.Criteria criteria,
                         Map<String,Object> gMap,Query query) {
        int sum=0;
        if (gMap!=null&&gMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : gMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andGreaterThan(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * 设置大于等于查询参数
     */
    private  int setGTMap(Class<T> clazz, Example.Criteria criteria,
                          Map<String,Object> gtMap,Query query) {
        int sum=0;
        if (gtMap!=null&&gtMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : gtMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andGreaterThanOrEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }

            }
        }
        return sum;
    }
    /**
     * 设置小于查询参数
     */
    private  int setLMap(Class<T> clazz, Example.Criteria criteria,
                         Map<String,Object> lMap,Query query) {
        int sum=0;
        if (lMap!=null&&lMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : lMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andLessThan(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置小于等于查询参数
     */
    private  int setLTMap(Class<T> clazz, Example.Criteria criteria,
                          Map<String,Object> ltMap,Query query) {
        int sum=0;
        if (ltMap!=null&&ltMap.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : ltMap.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.andLessThanOrEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置在内查询参数
     */
    private  int setInMap(Class<T> clazz, Example.Criteria criteria, Map<String,Iterable> inMap, Query query) {
        int sum=0;
        if (inMap!=null&&inMap.entrySet().size() > 0) {
            for (Map.Entry<String, Iterable> entry : inMap.entrySet()) {
                if(isJoinSqlIterable(query,clazz,entry)){
                    criteria.andIn(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置不在内查询参数
     */
    private  int setNotInMap(Class<T> clazz, Example.Criteria criteria, Map<String,Iterable> notInMap, Query query) {
        int sum=0;
        if (notInMap!=null&&notInMap.entrySet().size() > 0) {
            for (Map.Entry<String, Iterable> entry : notInMap.entrySet()) {
                if(isJoinSqlIterable(query,clazz,entry)){
                    criteria.andNotIn(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }


    /**
     * 设置模糊在查询参数(or)
     */
    private  int setOrLikeMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> likeParam, Query query) {
        int sum=0;
        if (likeParam!=null&&likeParam.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : likeParam.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orLike(entry.getKey(),"%" + entry.getValue().toString() + "%");
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置模糊不在查询参数(or)
     */
    private  int setOrNotLikeMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> likeParam, Query query) {
        int sum=0;
        if (likeParam!=null&&likeParam.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : likeParam.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orNotLike(entry.getKey(),"%" + entry.getValue().toString() + "%");
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置为null查询参数(or)
     */
    private  int setOrNullList(Class<T> clazz, Example.Criteria criteria, List<String> notNullList, Query query) {
        int sum=0;
        if (notNullList!=null&&notNullList.size() > 0) {
            for (String key: notNullList) {
                if(isJoinSqlKey(query,key,clazz)){
                    criteria.orIsNull(key);
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置不为null查询参数(or)
     */
    private  int setOrNotNullList(Class<T> clazz, Example.Criteria criteria, List<String> notNullList, Query query) {
        int sum=0;
        if (notNullList!=null&&notNullList.size() > 0) {
            for (String key: notNullList) {
                if(isJoinSqlKey(query,key,clazz)){
                    criteria.orIsNotNull(key);
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置等于查询参数(or)
     */
    private  int setOrToMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> toParams, Query query) {
        int sum=0;
        if (toParams!=null&&toParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : toParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * 设置不等于查询参数(or)
     */
    private  int setOrNotToMap(Class<T> clazz, Example.Criteria criteria, Map<String,Object> toParams, Query query) {
        int sum=0;
        if (toParams!=null&&toParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : toParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orNotEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置大于查询参数(or)
     */
    private  int setOrGMap(Class<T> clazz, Example.Criteria criteria,
                         Map<String,Object> gParams,Query query) {
        int sum=0;
        if (gParams!=null&&gParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : gParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orGreaterThan(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * 设置大于等于查询参数(or)
     */
    private  int setOrGTMap(Class<T> clazz, Example.Criteria criteria,
                          Map<String,Object> gtParams,Query query) {
        int sum=0;
        if (gtParams!=null&&gtParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : gtParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orGreaterThanOrEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }

            }
        }
        return sum;
    }
    /**
     * 设置小于查询参数(or)
     */
    private  int setOrLMap(Class<T> clazz, Example.Criteria criteria,
                         Map<String,Object> lParams,Query query) {
        int sum=0;
        if (lParams!=null&&lParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : lParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orLessThan(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置小于等于查询参数(or)
     */
    private  int setOrLTMap(Class<T> clazz, Example.Criteria criteria,
                          Map<String,Object> ltParams,Query query) {
        int sum=0;
        if (ltParams!=null&&ltParams.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : ltParams.entrySet()) {
                if(isJoinSqlObject(query,clazz,entry)){
                    criteria.orLessThanOrEqualTo(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置在内查询参数(or)
     */
    private  int setOrInMap(Class<T> clazz, Example.Criteria criteria, Map<String,Iterable> inParams, Query query) {
        int sum=0;
        if (inParams!=null&&inParams.entrySet().size() > 0) {
            for (Map.Entry<String, Iterable> entry : inParams.entrySet()) {
                if(isJoinSqlIterable(query,clazz,entry)){
                    criteria.orIn(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }
    /**
     * 设置不在内查询参数(or)
     */
    private  int setOrNotInMap(Class<T> clazz, Example.Criteria criteria, Map<String,Iterable> inParams, Query query) {
        int sum=0;
        if (inParams!=null&&inParams.entrySet().size() > 0) {
            for (Map.Entry<String, Iterable> entry : inParams.entrySet()) {
                if(isJoinSqlIterable(query,clazz,entry)){
                    criteria.orNotIn(entry.getKey(),entry.getValue());
                    sum++;
                }
            }
        }
        return sum;
    }



    private  Class<T> getClassT(){
        return  (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 是否添加到sql
     */
    private boolean   isJoinSqlObject( Query query,Class<T> clazz,
                                       Map.Entry<String,Object> entry ){
        try {
            Object value=entry.getValue();
            String key=entry.getKey();
            if(MyObjectUtil.noNullOrEmpty(value)&&clazz.getDeclaredField(key) != null){
                if(query!=null){
                    if(query.containsKey(key)){
                        query.remove(key);
                    }
                }
                return true;
            }
        }catch (NoSuchFieldException e){
        }
        return false;

    }
    /**
     * 是否添加到sql
     */
    private boolean   isJoinSqlIterable( Query query,Class<T> clazz,
                                         Map.Entry<String,Iterable> entry ){

        try {
            String key=entry.getKey();
            Iterable value=entry.getValue();
            if(MyObjectUtil.iterableCount(value)>0&&clazz.getDeclaredField(key) != null){
                if(query!=null){
                    if(query.containsKey(key)){
                        query.remove(key);
                    }
                }
                return true;
            }
        }catch (NoSuchFieldException e){
        }
        return false;

    }
    /**
     * 是否添加到sql
     */
    private boolean isJoinSqlKey(Query query,String key,Class<T> clazz){
        try {
            if(clazz.getDeclaredField(key) != null){
                if(query!=null){
                    if(query.containsKey(key)){
                        query.remove(key);
                    }
                }
                return true;
            }
        }catch (NoSuchFieldException e){
        }
        return false;
    }


    /**
     * 判断是否为不存在属性
     */
    public boolean isUselessKey(String key){
        Class<T> clazz=getClassT();
        try {
            if(clazz.getDeclaredField(key) != null){
                return false;
            }
        }catch (NoSuchFieldException e){
        }
        return true;
    }

    /**
     * 判断是否为无用集合
     * @param list
     * @param <K>
     * @return
     */
    private  <K> boolean isUselessList(List<K> list){
        if(list!=null){
            if( list.stream().filter(Objects::nonNull).collect(Collectors.toList()).size()>0){
                return false;
            }
        }
        return true;
    }
}
