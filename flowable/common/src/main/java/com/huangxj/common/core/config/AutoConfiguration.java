package com.huangxj.common.core.config;

import com.huangxj.common.core.exception.GlobalExceptionHandler;
import com.huangxj.common.core.exception.RestResponseErrorHandler;
import com.huangxj.common.core.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestTemplate;

/**
 * @ClassName AutoConfiguration
 * @Description TODO
 * @Author: huangxj
 * @Create: 2019-08-14 15:29
 * @Version V1.0
 **/
@Configuration
@Slf4j
public class AutoConfiguration {



    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("bean [{}]", holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    public GlobalExceptionHandler exceptionHandler() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        log.info("bean [{}]", exceptionHandler);
        return exceptionHandler;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        return restTemplate;
    }

}
