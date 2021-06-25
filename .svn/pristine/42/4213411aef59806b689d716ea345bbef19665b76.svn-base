package com.github.wxiaoqi.security.info.sys.mapper;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.info.sys.utils.VerifyCode;
import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMapper {
    @Autowired
    ArticleInfoMapper articleInfoMapper;

    /**
     * @Description: 测试删除文件
     * @Author: gsy
     * @Date: 2020/9/15
     */
    @Test
    public void testDel() {
        /*File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
//            flag = true;
        }*/
        File f = new File("F:\\infoSys\\123.jpg");
        FileUtil fileUtil = new FileUtil();
        boolean file = fileUtil.isFile(f);
        if (file) {
            FileUtil.del(f);
        }
    }
    /*@Test
    public void testArticleList(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",0);
        jsonObject.put("size",2);
        jsonObject.put("sysType","sys100");
        jsonObject.put("articleType","A01001");

        List<JSONObject> jsonObjects = articleInfoMapper.listArticle(jsonObject);
        System.out.println(jsonObjects);
    }*/

   /* @Test
    public void productValidateImg(HttpServletResponse response) throws IOException {
//        HttpServletResponse response = new HttpServletResponse();
        VerifyCode verifyCode = new VerifyCode();

        //使用字节流读取本地图片
        ServletOutputStream out=null;
        BufferedInputStream buf=null;
        //创建一个文件对象，对应的文件就是python把词云图片生成后的路径以及对应的文件名
        File file = new File("F:\\Test\\code.png");

        //输入流读取文件
        buf = new BufferedInputStream(new FileInputStream(file));
        //获取字节流输出对象
        out = response.getOutputStream();
        try {
            IOUtils.copy(buf,out);
        } finally {
            //关闭流
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(out);
        }


        verifyCode.output(verifyCode.getImage(), new FileOutputStream("test.jpg"));

        String str = verifyCode.getText();
        System.out.println(str);
    }
*/
    /*@Test
    public void fun1() throws FileNotFoundException, IOException {
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.output(verifyCode.getImage(), new FileOutputStream("F:\\Test\\code.png"));
        //验证码文字
        String str = verifyCode.getText();
        System.out.println(str);
    }*/
}
