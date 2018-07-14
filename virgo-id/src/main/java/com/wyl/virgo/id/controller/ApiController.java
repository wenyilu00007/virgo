package com.wyl.virgo.id.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.health.model.Check;
import com.ecwid.consul.v1.health.model.HealthService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version V1.0
 * @title: ApiController
 * @package com.wyl.zodiac.demo.controller
 * @description ${TO_DO}
 * @date 2017/9/7
 */
@RestController
public class ApiController {
    @Autowired
    private ConsulClient consulClient;

    Logger logger = LoggerFactory.getLogger(ApiController.class);
    List<String> serviceNodes = new ArrayList();

    {
        serviceNodes.add("10.39.232.218");
        serviceNodes.add("10.39.232.219");
        serviceNodes.add("10.39.232.220");
    }
    @ApiOperation(value = "根据服务名称，删除不健康节点")
    @RequestMapping(value = "/unregister/{id}", method = RequestMethod.GET)
    public String unregisterService(@PathVariable String id) {
        List<HealthService> response = consulClient.getHealthServices(id, false, null).getValue();
        for(HealthService service : response) {
            for(String serviceNode :serviceNodes){
                ConsulClient clearClient = new ConsulClient(serviceNode, 8500);
                // 创建一个用来剔除无效实例的ConsulClient，连接到无效实例注册的agent
                service.getChecks().forEach(check -> {
                    if(check.getStatus() != Check.CheckStatus.PASSING) {
                        logger.info("unregister : {}", check.getServiceId());
                        clearClient.agentServiceDeregister(check.getServiceId());
                    }
                });
            }
        }

        return "clear success";
    }

    @ApiOperation(value = "遍历所有 consul 节点，获取所有服务，然后删除不健康节点")
    @RequestMapping(value = "/unregister", method = RequestMethod.GET)
    public String unregisterServiceAll() {
        for(String serviceNode :serviceNodes) {
            ConsulClient consulClient = new ConsulClient(serviceNode, 8500);
            Response<Map<String, Service>> response = consulClient.getAgentServices();
            Map<String, Service> map = response.getValue();
            Collection<Service> services = map.values();
            for(Service service :services){
                logger.info("clear serivce:"+service.getService());
                this.unregisterService(service.getService());
            }
            System.out.println(response.toString());
        }
        return "clear success";
    }
}
