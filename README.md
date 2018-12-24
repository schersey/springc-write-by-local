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


九、Springcloud stream[rabbitmq、kafka]   [strim]
stream主要简化了消息应用的开发，该框架主要包括以下内容：
Stream框架自己的应用模型；
绑定抽象层，可以与消息代理中间件进行绑定；
持久化订阅的支持；
消息者组的支持；
topic分区的支持。
1、消息生产者方：（加在power服务端上）
a、pom.xml：（rabbitmq和kafka可以来换切换）
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-config -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
  <version>1.4.3.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-stream-rabbit -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
  <version>1.3.4.RELEASE</version>
</dependency>
<!--&lt;!&ndash; https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-stream-kafka &ndash;&gt;-->
<!--<dependency>-->
  <!--<groupId>org.springframework.cloud</groupId>-->
  <!--<artifactId>spring-cloud-starter-stream-kafka</artifactId>-->
  <!--<version>1.3.3.RELEASE</version>-->
<!--</dependency>-->

b、application.yml
spring:
  application:
    name: microserver-1000-client
  #rabbitmq
  rabbitmq:
    host: ***.***.***.***
    port: 5672
    username: root
    password: root123

c、service
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface RabbitMQSendService {

    @Output("myInput")
    SubscribableChannel sendOrder();

}

d、controller
import com.smart.service.RabbitMQSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by yanchangxian on 2018/12/23.
 */
@RestController
public class RabbitMQSendController {
    @Autowired
    RabbitMQSendService rabbitMQSendService;

    @RequestMapping(value = "/sendRabbitMQ", method = RequestMethod.GET)
    public String sendRequest(){
        //创建消息
        Message msg = MessageBuilder.withPayload("Hello World".getBytes()).build();
        //发送消息
        rabbitMQSendService.sendOrder().send(msg);
        return "send ok";
    }
}

e、启动类：
@SpringBootApplication
@EnableEurekaClient
@EnableBinding(RabbitMQSendService.class)
public class ApplicationPower {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationPower.class);
    }
}

f、启动后访问：http://localhost:1000/sendRabbitMQ  返回：send ok
2、消息消费者方（加在了client服务上）
a、pom.xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-config -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
  <version>1.4.3.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-stream-rabbit -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
  <version>1.3.4.RELEASE</version>
</dependency>
<!--&lt;!&ndash; https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-stream-kafka &ndash;&gt;-->
<!--<dependency>-->
<!--<groupId>org.springframework.cloud</groupId>-->
<!--<artifactId>spring-cloud-starter-stream-kafka</artifactId>-->
<!--<version>1.3.3.RELEASE</version>-->
<!--</dependency>-->

b、application.yml
spring:
  application:
    name: microserver-2000-client
  #rabbitmq
  rabbitmq:
    host: ***.***.***.***
    port: 5672
    username: root
    password: root123

c、service
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RabbitMQGetService {

    @Input("myInput")
    SubscribableChannel myInput();

}

d、启动类：
//@EnableHystrix
@EnableCircuitBreaker //hystrix
@EnableHystrixDashboard //hystrix控制台
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient

@EnableZuulProxy  //zuul
@EnableBinding(RabbitMQGetService.class)
public class ApplicationClient {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class, args);
    }


    @StreamListener("myInput")
    public void receive(byte[] msg) {
        System.out.println("get msg:" + new String(msg));
    }
}

e、控制台输出：
get msg:Hello World。

相比较而言，还是喜欢用原生的rabbitmq
https://note.youdao.com/web/#/file/WEB7badcd44ef6828cc5a40ec5e0a3205ed/note/WEB9ceb097f8959846ba0e3c6dce474ed51/

十、SpringCloud Config
通过对它们的配置，可以很好的管理集群中的配置文件，在实际应用时，我们会将配置文件存放到外部系统（git、svn中），springcloud config的服务器与客户端将到这些外部系统中读取、使用这些配置。
配置服务器主要有以下功能：
提供访问配置的服务接口；
对属性进行加密和解密；
可以简单的嵌入springboot中
配置客户端主要有以下功能：
绑定配置服务器，使用远程的属性来初始化spring容器；
对属性进行加密和解密；
属性改变时，可以对他们进行重新加载；
提供了与配置相关的几个管理端点等
Spring Cloud Config是Spring Cloud团队创建的一个全新项目，用来为分布式系统中的基础设施和微服务应用提供集中化的外部配置支持，它分为服务端与客户端两个部分。其中服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置仓库并为客户端提供获取配置信息、加密／解密信息等访问接口；而客户端则是微服务架构中的各个微服务应用或基础设施，它们通过指定的配置中心来管理应用资源与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息。Spring Cloud Config实现了对服务端和客户端中环境变量和属性配置的抽象映射，所以它除了适用于Spring构建的应用程序之外，也可以在任何其他语言运行的应用程序中使用。由于Spring Cloud Config实现的配置中心默认采用Git来存储配置信息，所以使用Spring Cloud Config构建的配置服务器，天然就支持对微服务应用配置信息的版本管理，并且可以通过Git客户端工具来方便地管理和访问配置内容。
https://blog.csdn.net/smartdt/article/details/79061906
https://blog.csdn.net/smartdt/article/details/79070943

十一、SpringCloud BUS 
在微服务架构的系统中，我们通常会使用轻量级的消息代理来构建一个共用的消息主题让系统中所有微服务实例都连接上来，由于该主题中产生的消息会被所有实例监听和消费，所以我们称它为消息总线。
我们经常需要使用消息代理的场景：
将消息路由到一个或多个目的地。
消息转化为其他的表现方式。
执行消息的聚集、消息的分解，并将结果发送到它们的目的地，然后重新组合响应返回给消息用户。
调用Web服务来检索数据。
响应事件或错误。
使用发布－订阅模式来提供内容或基千主题的消息路由。
目前已经有非常多的开源产品可以供大家使用， 比如：
ActiveMQ
Kafka
RabbitMQ
RocketMQ
等.....
https://blog.csdn.net/smartdt/article/details/79073765

十二、SpringCloud Sleuth框架 [sluθ] 
随着业务的发展，系统规模也会变得越来越大，各微服务间的调用关系也变得越来越错综复杂。通常一个由客户端发起的请求在后端系统中会经过多个不同的微服务调用来协同产生最后的请求结果，在复杂的微服务架构系统中，几乎每一个前端请求都会形成一条复杂的分布式服务调用链路，在每条链路中任何一个依赖服务出现延迟过高或错误的时候都有可能引起请求最后的失败。这时候，对于每个请求，全链路调用的跟踪就变得越来越重要，通过实现对请求调用的跟踪可以帮助我们快速发现错误根源以及监控分析每条请求链路上的性能瓶颈等。
针对上面所述的分布式服务跟踪问题，Spring Cloud Sleuth提供了 一套完整的解决方案。

目前有许多分布式跟踪系统，例如：zipkin、HTrace等，这些系统可以帮助我们收集一些由服务实时产生的数据，通过这些数据可以分析出分布式系统的健康状态，服务调用过程等。

1、整合Zipkin
https://blog.csdn.net/smartdt/article/details/79077110
springcloud2中各种报错。。。

