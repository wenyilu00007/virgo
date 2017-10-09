package com.hoau.virgo.id.controller;

import com.hoau.zodiac.core.util.id.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 刘德云
 * @version V1.0
 * @title: IdGenerateController
 * @package com.hoau.virgo.virgo.controller
 * @description id 生成接口服务
 * @date 2017/8/8
 */
@RestController
@RequestMapping(value = "/generator")
@Api(value = "/generator", description = "ID 生成微服务")
public class IdGenerateController {

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @ApiOperation(value = "获取主键", notes = "无需传入任何参数，产生全局唯一 ID，ID为类型为long，长度不定长")
    @GetMapping("/id")
    public String idProvider() {
        return String.valueOf(snowflakeIdWorker.nextId());
    }

}
