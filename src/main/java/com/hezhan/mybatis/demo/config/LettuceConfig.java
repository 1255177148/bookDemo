package com.hezhan.mybatis.demo.config;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @Author hezhan
 * @Date 2020/3/30 17:01
 * 开启拓扑刷新
 */
@Component
public class LettuceConfig implements LettuceClientConfigurationBuilderCustomizer {
    @Override
    public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
               // 开启自适应刷新
                .enableAllAdaptiveRefreshTriggers()
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(10))
                // 开启定时刷新，时间间隔根据实际情况修改
                .enablePeriodicRefresh(Duration.ofSeconds(15))
                .build();
        clientConfigurationBuilder.clientOptions(
                ClusterClientOptions.builder().topologyRefreshOptions(clusterTopologyRefreshOptions).build());
    }
}
