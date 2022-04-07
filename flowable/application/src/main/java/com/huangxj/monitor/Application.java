package com.huangxj.monitor;

import com.huangxj.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 *
 * @author huangxj
 * @create 2020-05-19
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(AppConstant.BASE_PACKAGES)
//@SpringBootApplication(scanBasePackages = AppConstant.BASE_PACKAGES)
@EnableSwagger2
@EnableCaching
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        //context.publishEvent(new MqttClientPreparedEvent(new Object()));
    }
}
