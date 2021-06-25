package org.springframework.cloud.alibaba.nacos.client;


import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.cloud.alibaba.nacos.NacosPropertySourceRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.*;

/**
 * 利用同包同类名替换nacos配置依赖中某个类(因为java类加载顺序是先加载项目再加载依赖)，设置nacos读取yaml时按某种编码，解决环境编码不同造成的问题。
 * 以后依赖该模块的也会继承此次替换
 */
public class NacosPropertySourceBuilder {
    //读取到配置文件的编码格式  NacosPropertySourceBuilder
    private String code="UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosPropertySourceBuilder.class);
    private static final Properties EMPTY_PROPERTIES = new Properties();
    private ConfigService configService;
    private long timeout;

    public NacosPropertySourceBuilder(ConfigService configService, long timeout) {
        this.configService = configService;
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ConfigService getConfigService() {
        return this.configService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    NacosPropertySource build(String dataId, String group, String fileExtension, boolean isRefreshable) {
        Properties p = this.loadNacosData(dataId, group, fileExtension);
        if (p == null) {
            p = EMPTY_PROPERTIES;
        }

        NacosPropertySource nacosPropertySource = new NacosPropertySource(group, dataId, this.propertiesToMap(p), new Date(), isRefreshable);
        NacosPropertySourceRepository.collectNacosPropertySources(nacosPropertySource);
        return nacosPropertySource;
    }

    private Properties loadNacosData(String dataId, String group, String fileExtension) {
        String data = null;

        try {
            data = this.configService.getConfig(dataId, group, this.timeout);
            if (!StringUtils.isEmpty(data)) {
                LOGGER.info(String.format("Loading nacos data, dataId: '%s', group: '%s'", dataId, group));
                if (fileExtension.equalsIgnoreCase("properties")) {
                    Properties properties = new Properties();
                    properties.load(new StringReader(data));
                    return properties;
                }

                if (fileExtension.equalsIgnoreCase("yaml") || fileExtension.equalsIgnoreCase("yml")) {
                    YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
                    System.out.println(code);
                    yamlFactory.setResources(new Resource[]{new ByteArrayResource(data.getBytes(code))});
                    return yamlFactory.getObject();
                }
            }
        } catch (NacosException var6) {
            LOGGER.error("get data from Nacos error,dataId:{}, ", dataId, var6);
        } catch (Exception var7) {
            LOGGER.error("parse data from Nacos error,dataId:{},data:{},", new Object[]{dataId, data, var7});
        }

        return null;
    }

    private Map<String, Object> propertiesToMap(Properties properties) {
        Map<String, Object> result = new HashMap(16);
        Enumeration keys = properties.propertyNames();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            Object value = properties.getProperty(key);
            if (value != null) {
                result.put(key, ((String)value).trim());
            } else {
                result.put(key, (Object)null);
            }
        }

        return result;
    }
}
