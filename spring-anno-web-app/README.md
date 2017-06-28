# How do I build the app

## Init the workspace for web application development using Maven

`mvn archetype:generate -DgroupId=com.superware -DartifactId=spring-anno-web-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false`

## Linux 上安装Tomcat

1. 获取Tomcat安装包

   `wget http://apache.etoak.com/tomcat/tomcat-7/v7.0.14/bin/apache-tomcat-7.0.14.tar.gz`

2. 解压并安装
   1. `mkdir -p /usr/local/webserver/`
   2. `tar  xvzf  apache-tomcat-7.0.14.tar.gz -C /usr/local/webserver/`
   3. 重命名安装目录
      * `cd /usr/local/webserver/`
      * `mv apache-tomcat-7.0.14/ tomcat`

3. 设置tomcat以独立的用户运行

   添加一个系统用户tomcat，并且设置为不可登录系统。

   `useradd -d /usr/local/webserver/tomcat -s /usr/sbin/nologin tomcat`

4. 设置用户tomcat对tomcat目录的访问权限

   `chown -R tomcat.tomcat /usr/local/webserver/tomcat/`

5. 配置Tomcat环境变量
   1. `vi /etc/environment`

      <p>CATALINA_BASE=/usr/local/webserver/tomcat<br>
      CATALINA_HOME=/usr/local/webserver/tomcat<br>
      TOMCAT_USER＝tomcat</p>

   2. 使用以下命令使配置生效

      `. /etc/environment`

6. 启动Tomcat

   `/usr/local/webserver/tomcat/bin/startup.sh`

   Sample output:
   ```
    Using CATALINA_BASE:   /usr/local/webserver/tomcat
    Using CATALINA_HOME:   /usr/local/webserver/tomcat
    Using CATALINA_TMPDIR: /usr/local/webserver/tomcat/temp
    Using JRE_HOME:        /usr
    Using CLASSPATH:       /usr/local/webserver/tomcat/bin/bootstrap.jar:/usr/local/webserver/tomcat/bin/tomcat-juli.jar
   ```
   
7. 测试

   访问http&#58;//ip:8080/，如果看到Tomcat缺省界面就表示成功了。

8. 停止Tomcat

   `/usr/local/webserver/tomcat/bin/shutdown.sh`

9. 设置Tomcat管理员帐号

   在目的标签前添加以下内容

   `nano /usr/local/webserver/tomcat/conf/tomcat-users.xml`

   ```xml
    <role rolename="admin-gui"/>
    <role rolename="admin-script"/>
    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>
    <role rolename="manager-jmx"/>
    <role rolename="manager-status"/>
    <user username="admin" password="000000" roles="manager-gui,manager-script,manager-jmx,manager-status,admin-script,admin-gui"/>
   ```
   
   保存关闭后，重新运行tomcat即可输入上面定交的用户名和密码，便可以登录Tomcat的管理页面。

10. 以守护进程方式运行tomcat

    按照tomcat官方的要求，tomcat作为一个守护进程运行，需要用到jsvc工具

    1. 安装jsvc

    ```
    cd  /usr/local/webserver/tomcat/bin/
    tar xvzf  commons-daemon-native.tar.gz
    cd commons-daemon-1.0.5-native-src/unix/
    ./configure
    make
    cp jsvc ../..
    cd ../..
    ```

    2. 运行下面的命令，便可以守护进程运行tomcat
    `cd  /usr/local/webserver/tomcat/`

    访问http&#58;//ip:8080/，如果看到Tomcat缺省界面就表示成功了。

11. 设置开机启动tomcat

    本打算以守护程序方式设置开机启动的，研究N久未果，先暂时用下面的方法吧!

    编辑/etc/rc.local，加入启动脚本
    `vi /etc/rc.local`

    `/usr/local/webserver/tomcat/bin/startup.sh`

    重启,访问http&#58;//ip:8080/，如果看到Tomcat缺省界面就表示成功了。

12. 开启GZIP

    修改%TOMCAT_HOME%/conf/server.xml，修订节点如下：
    ```xml
    <Connector port="80" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" URIEncoding="utf-8"
                       compression="on"
                       compressionMinSize="50" noCompressionUserAgents="gozilla, traviata"
                       compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain" />
    ```

13. 配置Tomcat线程池以使用高并发连接

    1. 打开共享的线程池：
    
    ```xml
    <Service name="Catalina">
    <!--The connectors can use a shared executor, you can define one or more named thread pools-->
    <Executor name="tomcatThreadPool" namePrefix="catalina-exec-" maxThreads="1000" minSpareThreads="50" maxIdleTime="600000"/>
    ```

    默认前后是注释<!-- -->掉的，去掉就可以了。

    重要参数说明：

    name：共享线程池的名字。这是Connector为了共享线程池要引用的名字，该名字必须唯一。默认值：None；

    namePrefix:在JVM上，每个运行线程都可以有一个name 字符串。这一属性为线程池中每个线程的name字符串设置了一个前缀，Tomcat将把线程号追加到这一前缀的后面。默认值：tomcat-exec-；

    maxThreads：该线程池可以容纳的最大线程数。默认值：200；

    maxIdleTime：在Tomcat关闭一个空闲线程之前，允许空闲线程持续的时间(以毫秒为单位)。只有当前活跃的线程数大于minSpareThread的值，才会关闭空闲线程。默认值：60000(一分钟)。

    minSpareThreads：Tomcat应该始终打开的最小不活跃线程数。默认值：25。

    threadPriority：线程的等级。默认是Thread.NORM_PRIORITY

    2. 在Connector中指定使用共享线程池：

    ```xml
    <Connector executor="tomcatThreadPool"
           port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
           minProcessors="5"
           maxProcessors="75"
           acceptCount="1000"/>
    ```

    重要参数说明：
    executor：表示使用该参数值对应的线程池；

    minProcessors：服务器启动时创建的处理请求的线程数；

    maxProcessors：最大可以创建的处理请求的线程数；

    acceptCount：指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理。

## nginx+redis+tomcat实现session共享的过程

1. nginx安装：http://blog.csdn.net/grhlove123/article/details/47834673

2. redis安装：http://blog.csdn.net/grhlove123/article/details/47783471

3. 准备两个tomcat，修改相应的端口

   名称 IP | 端口 | tomcat版本 | JDK
   ------ | ----- | --------- | ---
   tomcat1 | 10.10.49.23 | 8080 | 7.0.40 | 1.7.0_25
   tomcat2 | 10.10.49.15 | 8081 | 7.0.40 | 1.7.0_25

   修改nginx.conf加上：
   ```
    upstream backend {
        server 10.10.49.23:8080 max_fails=1 fail_timeout=10s;
        server 10.10.49.15:8081 max_fails=1 fail_timeout=10s;
    }
    ```

   修改nginx.conf的location成
   ```
    location / {
        root   html;
        index  index.html index.htm;
        proxy_pass http://backend;
     }
   ```
   
   启动nginx。
   下载tomcat-redis-session-manager相应的jar包，主要有三个：

   wget https://github.com/downloads/jcoleman/tomcat-redis-session-manager/tomcat-redis-session-manager-1.2-tomcat-7-java-7.jar
   
   wget http://central.maven.org/maven2/redis/clients/jedis/2.5.2/jedis-2.5.2.jar
   
   wget http://central.maven.org/maven2/org/apache/commons/commons-pool2/2.0/commons-pool2-2.0.jar

   下载完成后拷贝到$TOMCAT_HOME/lib中

   修改两tomcat的context.xml:
   ```xml
   <Context>

    <!-- Default set of monitored resources -->
    <WatchedResource>WEB-INF/web.xml</WatchedResource>

    <!-- Uncomment this to disable session persistence across Tomcat restarts -->
    <!--
    <Manager pathname="" />
    -->

    <!-- Uncomment this to enable Comet connection tacking (provides events
         on session expiration as well as webapp lifecycle) -->
    <!--
    <Valve className="org.apache.catalina.valves.CometConnectionManagerValve" />
    -->

   <Valve className="com.orangefunction.tomcat.redissessions.RedisSessionHandlerValve" />
   <Manager className="com.orangefunction.tomcat.redissessions.RedisSessionManager"
    host="10.10.49.20"
    port="6379"
    database="0"
    maxInactiveInterval="60" />
   </Context>
   ```

   在tomcat/webapps/test放一个index.jsp
   ```jsp
   <%@ page language="java" %>
   <html>
     <head><title>TomcatA</title></head>
     <body>
 
       <table align="centre" border="1">
         <tr>
           <td>Session ID</td>
           <td><%= session.getId() %></td>
         </tr>
         <tr>
           <td>Created on</td>
           <td><%= session.getCreationTime() %></td>
         </tr>
       </table>
     </body>
   </html>
   sessionID:<%=session.getId()%> 
   <br> 
   SessionIP:<%=request.getServerName()%> 
   <br> 
   SessionPort:<%=request.getServerPort()%> 
   <% 
   //为了区分，第二个可以是222
   out.println("This is Tomcat Server 1111"); 
   %>
   ```
   启动tomcat，发现有异常：com.orangefunction.tomcat.redissessions.RedisSessionHandlerValve 类找不到

   分别打开三个jar包，确实没有这个类，解决可以参考：

   http://blog.csdn.net/qinxcb/article/details/42041023

   通过访问http://10.10.49.20/test/ 可以看到不同的输出，不过session的create time 都是一样的
   
## Spring Session + Redis实现分布式Session共享

实际上实现Session共享的方案很多，其中一种常用的就是使用Tomcat、Jetty等服务器提供的Session共享功能，将Session的内容统一存储在一个数据库（如MySQL）或缓存（如Redis）中。我在以前的一篇博客中有介绍如何配置Jetty的Session存储在MySQL或MongoDB中。

本文主要介绍另一种实现Session共享的方案，不依赖于Servlet容器，而是Web应用代码层面的实现，直接在已有项目基础上加入Spring Session框架来实现Session统一存储在Redis中。如果你的Web应用是基于Spring框架开发的，只需要对现有项目进行少量配置，即可将一个单机版的Web应用改为一个分布式应用，由于不基于Servlet容器，所以可以随意将项目移植到其他容器。

Maven依赖

在项目中加入Spring Session的相关依赖包，包括Spring Data Redis、Jedis、Apache Commons Pool：
```xml
<!-- Jedis -->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
</dependency>
<!-- Spring Data Redis -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>1.7.3.RELEASE</version>
</dependency>
<!-- Spring Session -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
    <version>1.2.2.RELEASE</version>
</dependency>
<!-- Apache Commons Pool -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.4.2</version>
</dependency>
```
配置Filter

在web.xml中加入以下过滤器，注意如果web.xml中有其他过滤器，一般情况下Spring Session的过滤器要放在第一位。
```xml
<filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>ERROR</dispatcher>
</filter-mapping>
```
Spring配置文件
```xml
<bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration"/>
<bean class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
    <property name="hostName" value="localhost" />
    <property name="password" value="your-password" />
    <property name="port" value="6379" />
    <property name="database" value="10" />
</bean>
```
只需要以上简单的配置，至此为止即已经完成Web应用Session统一存储在Redis中，可以说是及其简单。

### 解决Redis云服务Unable to configure Redis to keyspace notifications异常

如果是自建服务器搭建Redis服务，以上已经完成了Spring Session配置，这一节就不用看了。不过很多公司为了稳定性、减少运维成本，会选择使用Redis云服务，例如阿里云数据库Redis版、腾讯云存储Redis等。使用过程中会出现异常：

Context initialization failed org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'enableRedisKeyspaceNotificationsInitializer' defined in class path resource [org/springframework/session/data/redis/config/annotation/web/http/RedisHttpSessionConfiguration.class]:
Invocation of init method failed; nested exception is java.lang.IllegalStateException: Unable to configure Redis to keyspace notifications.
See http://docs.spring.io/spring-session/docs/current/reference/html5/#api-redisoperationssessionrepository-sessiondestroyedevent
Caused by: redis.clients.jedis.exceptions.JedisDataException: ERR unknown command config
实际上这种异常发生的原因是，很多Redis云服务提供商考虑到安全因素，会禁用掉Redis的config命令： 
禁用config命令在错误提示链接的文档中，可以看到Redis需要以下的配置：

redis-cli config set notify-keyspace-events Egx
文档地址： 
http://docs.spring.io/spring-session/docs/current/reference/html5/#api-redisoperationssessionrepository-sessiondestroyedevent

首先要想办法给云服务Redis加上这个配置。

部分Redis云服务提供商可以在对应的管理后台配置： 
配置notify-keyspace-events如果不能在后台配置，可以通过工单联系售后工程师帮忙配置，例如阿里云.

完成之后，还需要在Spring配置文件中加上一个配置，让Spring Session不再执行config命令：

However, in a secured Redis enviornment the config command is disabled. This means that Spring Session cannot configure Redis Keyspace events for you. To disable the automatic configuration add ConfigureRedisAction.NO_OP as a bean.

配置：

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util
        http://www.springframework.org/schema/util/spring-util.xsd">

    <bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration"/>
    <bean class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
        <property name="hostName" value="localhost" />
        <property name="password" value="your-password" />
        <property name="port" value="6379" />
        <property name="database" value="10" />
    </bean>

    <!-- 让Spring Session不再执行config命令 -->
    <util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>

</beans>

