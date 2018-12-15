# springc-write-by-local
一、自己写微服务 12-13
1.通过RestTemplate完成单个微服务的代码实现。
2.(1)的实现通过nginx负载均衡来实现负载。
    弊端：
        1、如果出现多个业务，则需要不断的配置nginx；
        2、当集群中的某一台机器宕机后，系统不知道服务器运行情况；
        3、当业务路径或端口发生变化时，系统中需要修改内容较多，不利于维护。（在springcloud中利用Eureka注册组件，利用心跳机制来进行管理【注册与发现的过程】心跳默认30s）

二、通过springcloud实现微服务 12-15
1.在（一）的代码基础上增加对springcloud的依赖引入；
2.使用Eureka的server和client注解对服务进行区分；
3.通过Eureka平台查看服务运行情况；
4.通过springcloud-Ribbon中的@LoadBalanced注解实现负载。



