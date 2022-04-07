package com.huangxj.flowable.config;

import cn.hutool.core.collection.ListUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * BPM 模块的 Flowable 配置类
 *
 * @author jason
 */
@Configuration
@AutoConfigureAfter(FlowDataSourceConfig.class)
public class BpmFlowableConfiguration {

    @Autowired
    FlowDataSourceConfig flowDataSourceConfig;

    /**
     * BPM 模块的 ProcessEngineConfigurationConfigurer 实现类：
     * <p>
     * 1. 设置各种监听器
     * 2. 设置自定义的 ActivityBehaviorFactory 实现
     */
    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> bpmProcessEngineConfigurationConfigurer(ObjectProvider<FlowableEventListener> listeners) {

        return configuration -> {
            configuration.setEventListeners(ListUtil.toList(listeners.iterator()));
//            configuration.setDataSource(flowableDataSource());
            configuration.setDatabaseSchemaUpdate("true");
//            configuration.setDatabaseSchema("ischool-flowable");
            configuration.setActivityFontName("宋体");
            configuration.setLabelFontName("宋体");
            configuration.setAnnotationFontName("宋体");

        };
    }

    public DataSource flowableDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(flowDataSourceConfig.getJdbcUrl());
        dataSource.setPassword(flowDataSourceConfig.getPassword());
        dataSource.setUsername(flowDataSourceConfig.getUsername());
        dataSource.setDriverClassName(flowDataSourceConfig.getDriverClassName());
        return dataSource;
    }

}
