package com.hezhan.mybatis.demo.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author hezhan
 * @Date 2019/11/20 9:11
 */
@Configuration
public class OkHttpConfig {

    @Value("${http.connectTimeout}")
    private long connectTimeout;

    @Value("${http.readTimeout}")
    private long readTimeout;

    @Value("${http.writeTimeout}")
    private long writeTimeout;

    @Value("${http.maxIdleConnections}")
    private int maxIdleConnections;

    @Value("${http.keepAliveDuration}")
    private long keepAliveDuration;

    /**
     * 自定义OkHttp3 client配置
     *
     * @return OkHttp3 client
     */
    @Bean
    public OkHttpClient okHttpClient() {
//        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setMaxRequests(100);// 设置最大请求数
//        dispatcher.setMaxRequestsPerHost(100);// 设置单个ip最大请求数
        return new OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)// 设置连接超时时间
                .readTimeout(readTimeout, TimeUnit.SECONDS)// 设置读取超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)// 设置写超时时间
//                .dispatcher(dispatcher)// 配置分发数
                .connectionPool(pool())// 配置连接池
                .build();
    }

    /**
     * 连接池配置-配置最大空闲连接数、长连接超时时间
     *
     * @return 连接池
     */
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES);
    }
}
