package com.hoau.virgo.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * @author 刘德云
 * @version V1.0
 * @title: VirgoZipkinServerApplication
 * @package com.hoau.virgo.zipkin
 * @description
 * @date 2017/9/28
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinStreamServer
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, SessionAutoConfiguration.class})
public class VirgoZipkinServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirgoZipkinServerApplication.class, args);
    }
}
