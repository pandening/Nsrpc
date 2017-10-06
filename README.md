# ok-rpc

```java

ok-rpc是一个轻量级、学习型的rpc框架，使用了流行的NIO框架Netty作为底层数据传输介质，使用Spring
容器框架管理bean，使用Zookeeper来做服务注册和服务发现。未来还会添加更多有趣的技术到这里面来，所以ok-rpc的将一直不断的更新，或多或少的
添加新的内容，使其逐渐完善，并走向功能丰富、性能卓越的方向。

性能和功能点是目前ok-rpc框架首要考虑的关键点，目前的ok-rpc性能并不可观，资源占用非常大，日后需要
尽快不断的优化完善。

在设计上，ok-rpc希望一台机器部署一个ok-rpc的服务端或者客户端，而一个服务端希望可以使用一个[ip:pot]对外
提供服务，一个ok-rpc客户端则可以访问任意可达的服务，未来会做权限控制，关于checklist和todolist将在下文
中提到。

ok-rpc的服务端启动之初，就会加载所有注册的服务，服务想要对外发布就需要通过ok-rpc的服务端进行对外暴露，而
ok-rpc使用了Spring强大得bean管理能力接管了暴露服务的任务，同样客户端在启动之初同样会使用Spring的强大力
量来初始化service的引用，ok-rpc的服务端与客户端之间通过Netty进行数据传输（request和response），目前
仅仅支持基于Tpc的传输，而且协议极为简单，未来将重新设计通信协议，包括codec协商、loadBalance等等内容。
ok-rpc使用了各种技术来管理远端服务和客户端调用，使得服务端的服务暴露异常简单，客户端的调用就好像是在本地
调用一样。

ok-rpc的名字源于okHttp和okIo，对于ok开头的框架都是非常厉害的，介于希望ok-rpc也能逐渐走向牛逼之路，所以
取名字为“ok-rpc”，至于为什么中间添加了“-”，也就意味着离OkHttp、OkIo等框架还要很远的路要走。

```



## ok-rpc的使用


使用ok-rpc异常简单，下面是一个简单的示例。

1. 服务端暴露服务

你需要编写一个接口，比如下面的这个：

```java
package io.hujian.rpc.test.client;

/**
 * Created by HuJian on 2017/10/5.
 *
 */
public interface EchoService {

    String echo(String msg);

}

```

对于服务端来说，你还需要编写实现类：

```java
package io.hujian.rpc.test.server;

import io.hujian.npc.manager.RpcService;
import io.hujian.rpc.test.client.EchoService;

/**
 * Created by HuJian on 2017/10/5.
 *
 * impl
 */
@RpcService(EchoService.class)
public class EchoServiceImpl implements EchoService{
    @Override
    public String echo(String msg) {
        return "Echo:" + msg;
    }
}

```

需要注意的一点是，你需要在你的接口的实现类上加上@RpcService注解，让ok-rpc知道这是一个ok-rpc接口的实现类。至于
具体的接口，你需要添加在注解的参数中。

最后一步，当然是使用Spring的xml文件来主持bean了：

```java

  <bean id="nodeList" class="io.hujian.npc.pubisher.RpcNodeGroup">
        <property name="nodeGroupName" value="test-node-list-group"/>
        <property name="nodeList" value="127.0.0.1:18999:1"/>
    </bean>
    
      <bean id="echoService" class="io.hujian.npc.pubisher.RpcServicePublishBean">
        <property name="nodeGroup" ref="nodeList"/>
        <property name="interfaceName" value="EchoService"/>
        <property name="url" value="io.hujian.rpc.test.client.EchoService"/>
        <property name="version" value="1.0.0"/>
    </bean>  
```

不要以为nodeList取名叫List就可以使用多个地址，目前来说完全只支持一个地址，上文已经说了ok-rpc的设计考虑。经过这几步，现在
你可以加载这个xml文件来启动你的rpc-server了，启动rpc-server也使用了Spring，下面是一个示例文件：

```java

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <context:component-scan base-package="io.hujian.*"/>

    <!-- load config file -->
    <bean id="propertyConfigurer"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:zkConfig.properties</value>
            </list>
        </property>
    </bean>

    <!-- The Rpc Discover Service -->
    <bean id="serviceDiscovery" class="io.hujian.npc.discover.ServiceDiscover"/>

    <bean id="rpcClient" class="io.hujian.npc.manager.RpcClient">
        <constructor-arg name="discover" ref="serviceDiscovery"/>
    </bean>

    <bean id="serviceRegistry" class="io.hujian.npc.register.SampleServiceRegister"/>

    <bean id="rpcServer" class="io.hujian.npc.manager.RpcServer"/>

</beans>

```

使用Spring加载上述文件即可启动ok-rpcserver，比如这个文件叫ApplicationContext.xml,则可以使用下面的代码
来启动ok-rpc服务器：

```java
        new ClassPathXmlApplicationContext("ApplicationContext.xml");

```

2. 客户端注册服务bean

首先，你需要有一份服务端提供的接口类文件，比如，你想要访问上面注册的EchoService，那么你就需要有一个这样的接口类。然后
你通过注册下面的bean来加载引用：

```java

    <bean id="echoService" class="io.hujian.npc.consumer.RpcServiceReferenceBean"
          init-method="init">
        <property name="clazz" value="io.hujian.rpc.test.client.EchoService"/>
    </bean>

```

然后，你就可以使用这个bean了：

```java

        ApplicationContext context = new ClassPathXmlApplicationContext("client-services.xml");
        EchoService echoService = (EchoService) context.getBean("echoService");

        System.out.println("Response:" + echoService.echo("hello, ok-rpc!"));

```


## 测试

|   Request 数量 | Response数量 | 花费时间 | qps | 客户端使用线程数量 |
|---------------|--------------|-----------|----|--------|
|160000| 160000| 22473ms|7119|16|
|320000|320000|37836ms|8457|16|
|800000|800000|102226|7825|

服务端运行时资源：

![服务端](https://github.com/pandening/Nsrpc/blob/master/imgs/client-load.png)

客户端运行时资源：

![客户端](https://github.com/pandening/Nsrpc/blob/master/imgs/client-load.png)


测试机器：MacBook Pro， 2.2GHz Intel Core i7 16GB 1600MHz DDR3


## Checklist & Todolist

```java

   1、性能优化，提升qps
   2、添加服务暴露时的版本控制，可提供多个版本的统一服务
   3、服务端-客户端协议重新设计
   4、更多的编解码器设计
   5、代码格式调整、组织结构调整
   6、bug检测、修复


```


## 更新日志


```java

[2017-10-06 11:03] 项目移动，init，readme 


```

## License

```java

Copyright 2017 HuJian

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
