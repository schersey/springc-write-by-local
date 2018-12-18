# springc-write-by-local
一、自己写微服务 (12-13)
1.通过RestTemplate完成单个微服务的代码实现。
2.(1)的实现通过nginx负载均衡来实现负载。
    弊端：
        1、如果出现多个业务，则需要不断的配置nginx；
        2、当集群中的某一台机器宕机后，系统不知道服务器运行情况；
        3、当业务路径或端口发生变化时，系统中需要修改内容较多，不利于维护。（在springcloud中利用Eureka注册组件，利用心跳机制来进行管理【注册与发现的过程】心跳默认30s）

二、通过springcloud实现微服务 (12-15)
1.在（一）的代码基础上增加对springcloud的依赖引入；
2.使用Eureka的server和client注解对服务进行区分；
3.通过Eureka平台查看服务运行情况；
4.通过springcloud-Ribbon中的@LoadBalanced注解实现负载。

三、加入Springboot actuator (12-17)
springboot actuator模块主要用于系统监控，当应用程序整合了Actuator后，它就会自动提供多个服务端点
1、加在了power1、power2、clent上。
2、在client中加入服务查询事项：（加载client端）
配置文件加入：访问：http://localhost:2000/actuator/health
management:
 endpoint:
   health:
     show-details: always #健康情况-展示全部详情，否则只展示status信息


查找服务列表和状态：
访问：http://localhost:2000/router
控制台：
MICROSERVER-1000-CLIENT---bogon:microserver-1000-client:1000---UP
MICROSERVER-2000-CLIENT---bogon:microserver-2000-client:2000---UP

四、加入springcloud Ribbon(@LoadBalanced) (12-18)
在client中写请求
在power1&2中写具体实现，请求后：
{"id":1,"name":"yan-power1","age":30,"message":"/person/1"}
{"id":1,"name":"yan-power2","age":30,"message":"/person/1"}
来回变换

@Bean
@LoadBalanced
RestTemplate restTemplate() {
    return new RestTemplate();
}

六、SpringCloud Feign [fen] (12-18)
Feign是一个声明式的web服务客户端，它使得写web服务变得更简单。使用Feign,只需要创建一个接口并注解。
例如：
@FeignClient(value = "MICROSERVER-1000-CLIENT") //注册到eureka中的实例名
public interface FeignService {
    @RequestMapping("/index.do") //实例名中具体的请求路径（一定要保持一致）
    String testFeign();
}

启动类：
@EnableFeignClients //添加Feign的Enable
@SpringBootApplication
@EnableEurekaClient
public class ApplicationClient {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class, args);
    }
}

请求类：
@RestController
public class FeignController {

    static final Logger log = LoggerFactory.getLogger(FeignController.class);

    @Autowired
    FeignService feignService;  //获得server

    @RequestMapping(value = "/testFeign.do", method = RequestMethod.GET)
    private String feign(){
        return feignService.testFeign();  //请求
    }
}

访问：http://localhost:2000/testFeign.do
[{"port":"1001","keyTest2":"测试2的值～"}]
[{"keyTest":"测试的值～","port":"1000"}]
来回变换。。。
2、feign与ribbon的区别
https://blog.csdn.net/smartdt/article/details/79034200
Ribbon是一个基于HTTP和TCP客户端的负载均衡器。Feign中也使用Ribbon。
Ribbon可以在通过客户端中配置的ribbonServerList服务端列表去轮询访问以达到均衡负载的作用。
当Ribbon与Eureka联合使用时，ribbonServerList会被DiscoveryEnabledNIWSServerList重写，扩展成从Eureka注册中心中获取服务端列表。同时它也会用NIWSDiscoveryPing来取代IPing，它将职责委托给Eureka来确定服务端是否已经启动。

Feign是一个声明式的Web Service客户端，它使得编写Web Serivce客户端变得更加简单。我们只需要使用Feign来创建一个接口并用注解来配置它既可完成。它具备可插拔的注解支持，包括Feign注解和JAX-RS注解。Feign也支持可插拔的编码器和解码器。Spring Cloud为Feign增加了对Spring MVC注解的支持，还整合了Ribbon和Eureka来提供均衡负载的HTTP客户端实现。
我们使用Feign提供的注解编写HTTP接口的客户端代码非常简单, 只需要声明一个Java接口加上少量注解即可完成。
Feign会帮我们处理好一切. 根据我们的接口声明, Feign会在Spring容器启动之后, 将生成的代理类注入, 所以我们不需要写HTTP调用的实现代码就能完成REST接口的调用。
Feign服务客户端定义的请求url必须与服务提供者url一致。
Feign服务客户端中的接口名、返回对象可以任意定义。但对象中的属性类型和属性名必须一致，与两个对象中的属性顺序和数量无关。
启动 Eureka注册中心、服务提供者、Feign服务客户端，然后 Eureka注册中心挂掉时，Feign服务客户端消费服务是不受影响的。





