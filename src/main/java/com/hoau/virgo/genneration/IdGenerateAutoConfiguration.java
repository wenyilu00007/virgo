package com.hoau.virgo.genneration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 刘德云
 * @version V1.0
 * @title: IdGenerateAutoConfiguration
 * @package com.hoau.virgo.virgo.genneration
 * @description
 * @date 2017/8/8
 */
@Configuration
@EnableConfigurationProperties(IdGenerateProperties.class)
public class IdGenerateAutoConfiguration {

    @Autowired
    IdGenerateProperties idGenerateProperties;

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeIdWorker init(){
        return new SnowflakeIdWorker(idGenerateProperties.getWorkId(),idGenerateProperties.getDatacenterId());
    }

}
