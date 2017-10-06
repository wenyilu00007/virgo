# 链式追踪 zipkinServer
###前言
   针对服务化应用全链路追踪的问题，Google发表了Dapper论文，介绍了他们如何进行服务追踪分析。其基本思路是在服务调用的请求和响应中加入ID，标明上下游请求的关系。利用这些信息，可以可视化地分析服务调用链路和服务间的依赖关系。
Zipkin是 Twitter 对 Dapper 的实现，支持多种语言。
###Spring Cloud sleuth
Spring Cloud Sleuth是对Zipkin的一个封装，对于Span、Trace等信息的生成、接入HTTP Request，以及向Zipkin Server发送采集信息等全部自动完成。

