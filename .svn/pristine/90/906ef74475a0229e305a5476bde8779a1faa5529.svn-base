package com.github.wxiaoqi.security.consulation.mapper;

import com.github.wxiaoqi.security.consultation.entity.BiConsultation;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.BiexpertUser;
import com.github.wxiaoqi.security.consultation.entity.User;
import com.github.wxiaoqi.security.consultation.mapper.BiConsultationMapper;
import com.github.wxiaoqi.security.consultation.mapper.BiExpertMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {

    @Autowired(required = false)
    BiExpertMapper biExpertMapper;

    @Autowired(required = false)
    BiConsultationMapper biConsultationMapper;

    @Test
    public void test(){
//        BiExpert biExpert = biExpertMapper.selectOneExpert(25);
//        System.out.println(biExpert);
    }
    @Test
    public void test02(){
        BiexpertUser biexpertUser = biExpertMapper.selectOneExpertUser(1);
        System.out.println(biexpertUser);
    }
//    测试删除
    @Test
    public void test03(){

        try {
            biExpertMapper.deleteOneExpert(61);
            System.out.println("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test04(){
        List<BiConsultation> biConsultations = biConsultationMapper.aksListByExpertId(1);
        System.out.println(biConsultations);

    }
}
