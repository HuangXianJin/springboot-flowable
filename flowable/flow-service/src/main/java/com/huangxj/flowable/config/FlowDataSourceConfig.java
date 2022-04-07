package com.huangxj.flowable.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author huangxj
 * @version 1.0
 * @date 2022/3/28 11:29
 */
@Configuration
@ConfigurationProperties(prefix = "flowable.datasource")
public class FlowDataSourceConfig {

    private String jdbcUrl="jdbc:sqlserver://127.0.0.1:26888;DatabaseName=FaceOpen";
    private String username="root";
    private String password="123456";
    private String driverClassName="com.mysql.cj.jdbc.Driver";



    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
