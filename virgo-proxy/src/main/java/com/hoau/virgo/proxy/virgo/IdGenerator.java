package com.hoau.virgo.proxy.virgo;

import com.hoau.virgo.proxy.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 刘德云
 * @version V1.0
 * @title: service
 * @package com.hoau.zodiac.core
 * @description 主键生成服务
 * @date 2017/8/9
 */
@FeignClient(value = Constants.VIRGO_SERVICE_NAME)
public interface IdGenerator {

    @RequestMapping(value = Constants.VIRGO_ID_GENERATOR_PATH, method = RequestMethod.GET)
    String nextId();
}
