## 主键生成服务

   - 项目名virgo['və:ɡəu]为十二星座的处女座，处女座最求完美，寓意ID生成服务是完美主义者，绝不会提供两个相同的ID

## 项目介绍
   - 项目生成 ID 核心逻辑由SnowflakeIdWorker完成
   - jar 启动命令中必须指定 workid 及 datacenterid,否则项目不能启动（报错信息如下），并且确保 workid+datacenterid 唯一，否则会导致生成ID可能重复
 >> org.springframework.validation.BindException: org.springframework.boot.bind.RelaxedDataBinder$RelaxedBeanPropertyBindingResult: 2 errors
Field error in object 'id.generate' on field 'workId': rejected value [${workid}]; codes [typeMismatch.id.generate.workId,typeMismatch.workId,typeMismatch.int,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [id.generate.workId,workId]; arguments []; default message [workId]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'int' for property 'workId'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [int]]
Field error in object 'id.generate' on field 'datacenterId': rejected value [${datacenterid}]; codes [typeMismatch.id.generate.datacenterId,typeMismatch.datacenterId,typeMismatch.int,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [id.generate.datacenterId,datacenterId]; arguments []; default message [datacenterId]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'int' for property 'datacenterId'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [int]]
	at org.springframework.boot.bind.PropertiesConfigurationFactory.checkForBindingErrors(PropertiesConfigurationFactory.java:359) ~[spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
  - 接口文档可以查看/virgo/swagger-ui.html
