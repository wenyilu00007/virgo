package com.hoau.virgo.mail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hoau.*")
@ComponentScan(basePackages = "com.hoau.*")
@MapperScan("com.hoau.**.dao")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, SessionAutoConfiguration.class})
public class VirgoSendMailApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirgoSendMailApplication.class, args);
	}
}
