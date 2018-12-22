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


七、断路器--集群保护框架--Hystrix [hɪst'rɪks]海斯特克斯 (12-19~12-22)
在很多系统架构中都需要考虑横向扩展、单点故障等问题，对于一个庞大的应用集群，部分服务或者机器出现问题不可避免，在出现故障时，如何减少故障、保障集群的高可用，成为一个重要的课题，Springcloud中集群保护框架：Hystrix。
在微服务架构中，存在着那么多的服务单元，若一个单元出现故障，就很容易因依赖关系而引发故障的蔓延，最终导致整个系统的瘫痪，这样的架构相较传统架构更加不稳定。为了解决这样的问题，产生了断路器等一系列的服务保护机制。
Spring Cloud Hystrix实现了断路器、线程隔离等一系列服务保护功能。它也是基于Netflix的开源框架Hystrix实现的，该框架的目标在于通过控制那些访问远程系统、服务和第三方库的节点， 从而对延迟和故障提供更强大的容错能力。Hystrix具备服务降级、服务熔断、线程和信号隔离、请求缓存、请求合并以及服务监控等强大功能。

在系统中访问http://localhost:2000/indexClient.do地址，在集群环境中，如果其中的一台发生故障（在eureka心跳期内）导致访问失败：
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.
Wed Dec 19 11:06:48 CST 2018
There was an unexpected error (type=Internal Server Error, status=500).
I/O error on GET request for "http://microserver-1000-client/person/1": Connection refused (Connection refused); nested exception is java.net.ConnectException: Connection refused (Connection refused)

A、在springcloud中使用hystrix -- client项目中
1、pom.xml：
<!-- 加入hystrix的依赖 -->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-hystrix -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-hystrix</artifactId>
  <version>1.4.3.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-hystrix-dashboard -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
  <version>1.4.3.RELEASE</version>
</dependency>

2、在springcloud2中要加入一项配置类：（不然会出现找不到hystrix.stream的情况）
@Bean
public ServletRegistrationBean getServlet() {
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
}

3、启动类中配置：
//@EnableHystrix
@EnableCircuitBreaker //hystrix
@EnableHystrixDashboard //hystrix控制台

4、访问地址：http://localhost:2000/hystrix.stream
【如果系统没有被使用的话会出现一直出ping：但是没有数据的情况，这时需要访问项目中的具体业务后才能有具体数据出现。】
5、hystrix控制台地址：http://localhost:2000/hystrix  输入：http://localhost:2000/hystrix.stream和title进行监控。
Hystrix主要保护调用服务的一放，如果被调用的服务发生故障，符合一定条件，就开启断路器，对调用的程序进行隔离。（可配置回退方式，这样当被调用服务器发生故障可以触发出回退方法中的内容）
6、Hystrix与Fegin结合
fegin:
  hystrix:
    enabled: true  #打开 feign与hystrix开关

@FeignClient(value = "MICROSERVER-1000-CLIENT", fallback = FeignService.FallBack.class)
public interface FeignService {
    @RequestMapping("/index.do")
    String testFeign();

    //如果失败 则走fallback方法
    @Component
    static class FallBack implements FeignService{

        @Override
        public String testFeign() {
            return "error hello";
        }
    }
}



八、Springcloud Zuul【祖鲁】-- 可以做路由中间件 (12-22)
Zuul为微服务提供代理、过滤、路由器等功能。可以帮我们实现以下功能：
身份验证和安全性过滤等；
观察和监控，跟踪重要数据等；
动态路由，将请求路由到不同的服集群；
负载均衡能力；
静态响应处理能力；
路由多样化。
简单来说，就是既具备路由转发功能，又具备过滤器功能，比如将/aaa/**路径请求转发到service-ribbon服务上，将/bbb/***路径请求转发到service-feign服务上，比如过滤，对请求参数的信息进行过滤，不符合的进行过滤拦截等。
1、pom.xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-zuul -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-zuul</artifactId>
  <version>1.4.3.RELEASE</version>
</dependency>

2、application.yml（如果是单独启模块的话，需要在eureka中注册）
zuul:
  routes:
    api-a:
      path: /ribbon/**
      serviceId: service-ribbon  #如果是/ribbon/**路径下的请求，则跳转到service-ribbon
    api-b:
      path: /feign/**
      serviceId: service-feign  #如果是/feign/**路径下的请求，则跳转到service-feign

3、过滤器类
public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("send {} request to{}", request.getMethod(), request.getRequestURL().toString());
        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null) {
            log.warn("accessToken is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("accessToken is empty");
            } catch (Exception e) {
            }
            return null;
        }
        log.info("access is ok");
        return null;
    }
}

4、注入Bean
@Bean
public AccessFilter accessFilter() {
    return new AccessFilter();
}

5、访问：
例如：http://localhost:2000/ribbon/hello 返回accessToken is empty
因为过滤器中要求加入accessToken的参数。
http://localhost:2000/ribbon/hello?accessToken=ribbon 则会访问ribbon服务名下的服务
http://localhost:2000/ribbon/hello?accessToken=feign 则会访问feign服务下的服务。

通过以上的测试，可以得出Zuul的路由和过滤都起作用了。
还可以配置zuul和ribbon、zuul和hystrix的整合、还有自定义过滤器等。



