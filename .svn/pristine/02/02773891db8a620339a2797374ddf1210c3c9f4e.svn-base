package com.github.wxiaoqi.security.common.handler;

import com.github.wxiaoqi.security.common.constant.CommonConstants;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.FormEncoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * 自定义处理方式 feign
 */
public class FeignSpringFormEncoder extends FormEncoder {


    public FeignSpringFormEncoder() {
        this(new Default());
    }


    public FeignSpringFormEncoder(Encoder delegate) {
        super(delegate);

        MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
        processor.addFirstWriter(new SpringSingleMultipartFileWriter());
        processor.addFirstWriter(new SpringManyMultipartFilesWriter());
    }


    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

        if (bodyType.equals(MultipartFile.class)) {
            MultipartFile file = (MultipartFile) object;
            if(file!=null){
                Map data = Collections.singletonMap(CommonConstants.FILE_PARAM_NAME, object);
                super.encode(data, MAP_STRING_WILDCARD, template);
            }
            return;
        } else if (bodyType.equals(MultipartFile[].class)) {
            MultipartFile[] file = (MultipartFile[]) object;
            if(file != null&&file.length!=0) {
            //    Map data = Collections.singletonMap(file.length == 0 ? "" : file[0].getName(), object);
                Map data = Collections.singletonMap(CommonConstants.FILES_PARAM_NAME, object);
                super.encode(data, MAP_STRING_WILDCARD, template);
            }
            return;
        }
        super.encode(object, bodyType, template);
    }
}
