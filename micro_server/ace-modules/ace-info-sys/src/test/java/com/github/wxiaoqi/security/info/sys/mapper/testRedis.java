package com.github.wxiaoqi.security.info.sys.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: 测试redis连接
 * @author: gsy
 * @create: 2020-09-17 17:17
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class testRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        //新增一个字符串类型的值，key是键，value是值。
        stringRedisTemplate.opsForValue().set("test", "gsy");
        System.out.println(stringRedisTemplate.opsForValue().get("test"));

        System.out.println(stringRedisTemplate.opsForValue().get("AG:AUTH:CLIENT:PRI"));

    }

}
