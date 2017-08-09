package com.hoau.virgo.genneration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 刘德云
 * @version V1.0
 * @title: IdGenerateAutoConfiguration
 * @package com.hoau.virgo.virgo.genneration
 * @description ID 生成器配置类
 * @date 2017/8/8
 */
@ConfigurationProperties(prefix = "id.generate")
public class IdGenerateProperties {

    /**
     * 机器编号
     */
    private int workId;
    /**
     * 数据中心编号
     */
    private int datacenterId;

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(int datacenterId) {
        this.datacenterId = datacenterId;
    }
}
