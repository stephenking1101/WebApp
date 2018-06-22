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
                   useSendfile="false"
                   compression="on"
                   compressionMinSize="50"
                   noCompressionUserAgents="gozilla,traviata"
                   compressableMimeType="text/html,text/xml,text/plain,text/css,text/javascript,application/javascript" />
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

## 改变Tomcat的ROOT目录
	
修改conf/server.xml，找到</Host>标签，在之前加入这样一行：<Context path="" docBase="F:/MyWeb" debug="0" reloadable="true" crossContext="true" />。

重启Tomcat，OK。

对上面语句做下解释：该句是设置Tomcat的虚拟路径，书写语法是<Context path="虚拟目录" docBase="实际目录" debug="0" reloadable="true" crossContext="true" />，我将网站实际根目录映射到了F:/MyWeb，于是更改了网站跟目录的映射。

这种修改方式的结果是：localhost依然是最初的webapps，但网站的根目录是F:/MyWeb，相当于把原始的ROOT目录映射成F:/MyWeb，以后写的网站直接放到F:/MyWeb下，运行http://localhost:8080/index.jsp，就能访问了。而且，由于localhost的路径没变，所以Tomcat Manager可以继续使用。
	
## Tomcat发布项目

在tomcat服务器的conf\Catalina\localhost目录下创建一个xml文件(路径找不到就自己创建)，内容如下：

```
　　　　<Context path="/TestPro" docBase="D:\javaProject\TestPro\WebContent" debug="0" privileged="true">
　　　　</Context>
```

其中path是指项目的发布路径，也就是访问路径，假如像上边那样填写，就要这样访问：http://localhost:8080/TestPro

docBase是指项目的目录，很好理解，你的项目最终发布，就是发布的这个目录，通过配置，直接让tomcat指向这个目录，这样就可以运行项目啦。

debug 为设定debug的等级0提供最少的信息,9提供最多的信息。

reloadable=true时 当web.xml或者class有改动的时候都会自动重新加载不需要从新启动服务。

crosscontext="true"表示配置的不同context共享一个session。

privileged="true" 意味着 Tomcat 自身的应用，比如· Tomcat Manager ，可以被当前这个应用访问。根据官方文档的解释，这个机理是改变应用的类加载器为 Server class loader 。我想，这种改变，会令应用程序发现 Tomcat 本身的类，都能够从应用自己的类加载器上寻找到。从而实现对 Tomcat 自身应用程序方法的调用。

注意：xml的文件名一定要和发布路径一致！在本例中xml文件名必须为：TestPro

* tomcat部署服务的四种方式

1. 利用tomcat的自动部署

 此种方式最为简单，我们只需要将web应用放到tomcat的webapps目录下即可！

2. 利用控制台补部署 

3. 增加自定义部署文件

此种方式是在tomcat 的根目录下的\conf\Catalina\localhost目录下，添加一个任意名称的xml文件，此文件名将作为web应用的虚拟路径。

内容如下：<Context docBase="D:\xxx" debug="0" reloadable="true" crossContext="true" />

4. 修改server.xml文件，部署web应用

在tomcat 的根目录下的\conf\目录下，修改server.xml文件，添加

<Context path="/xxx" docBase="D:\xxx" debug="0" reloadable="true" crossContext="true" />
	
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
```xml
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
```

## Apache http Server与Tomcat整合 

既然 Tomcat 本身已经可以提供这样的服务，我们为什么还要引入 Apache 或者其他的一些专门的 HTTP 服务器呢？原因有下面几个：
Apache的HTTPD是目前比较受欢迎的网站服务器软件，它不 但功能强大，而且完全免费，并且支持市场上流行的各种操作系统(Windows,Linux,Mac os)。同时对于Java Servlet/JSP的支持，通常也会使用同样Apache出品的Tomcat。
Tomcat除了支持Java Servlet/JSP之外，也可以当做网站服务器使用，但是在对于静态的html文件、图片文件等的解析效率上不如Apache HTTPD的执行效率高。应用tomcat的服务器如果网站的访问量较大，系统资源占用会明显升高，近日笔者在项目执行过程中遇到这一问题，便也想到同时 应用tomcat+apache服务。Apache负责静态资源处理，tomcat负责jsp和java servlet等动态资源的处理。

整合之后的好处是：
1. 提升对静态文件的处理性能
2. 利用 Web 服务器来做负载均衡以及容错
3. 无缝的升级应用程序

原理:
tomcat 为一个servelet容器，apache为一个web server，两者之间通信通过mod_jk的模块（由web服务器像apache、iis等使用）和Web Server通信，Tomcat 默认的 AJP Connector 的端口是 8009.整个过程其实就是让apache的httpd.conf文件调用mod_jk.conf，mod_jk.conf调用workers.properties，最后配置虚拟主机。
文件说明 ：
mod_jk.conf
主要定义mod_jk模块的位置以及mod_jk模块的连接日志设置，还有定义worker.properties文件的位置。 
worker.properties 
定义worker的参数，主要是连接tomcat主机的地址和端口信息。如果Tomcat与apache不在同一台机器上，或者需要做多台机器上tomcat的负载均衡只需要更改workers.properties文件中的相应定义即可。
％APACHE_HOME％为你的安装目

整合
1. 准备下载下列文件：
Jdk1.5 
下载地址: http://java.sun.com 
tomcat 5.5.9 
下载地址：http://jakarta.apache.org 
apache_2.2.4-win32-x86-no_ssl.msi 
下载地址: http://httpd.apache.org/download.cgi 
mod_jk－apache-2.2.3.so
下载地址：http://archive.apache.org/dist/jakarta/tomcat-connectors/
2. 安装好Jdk、tomcat、apache后,加入mod_jk连接模块，就是把mod_jk－apache-2.2.3.so文件重名为mod_jk.so文件并拷贝到％APACHE_HOME％"modules下
3. 修改apache的配置文件：
为了保持httpd.conf文件的简洁，把jk模块的配置放到单独的文件中来，就在httpd.conf中增加一行调用 
代码  include ％APACHE_HOME％Apache2"conf"mod_jk.conf
4. 配置mod_jk.conf 
请注意使用绝对路径 
其实最关键的就是 第一条 第二条 和最后一条，如果要精简，就保留这三条内容就可以了。 
代码 
```
# Load mod_jk module
LoadModule jk_module "E:"Program Files"Apache Software Foundation"Apache2.2"modules"mod_jk.so"
# Where to find workers.properties
JkWorkersFile "E:"Program Files"Apache Software Foundation"Apache2.2"conf"workers.properties"
# Where to put jk logs
JkLogFile "E:"Program Files"Apache Software Foundation"Apache2.2"logs"mod_jk.log"
# Set the jk log level [debug/error/info]
JkLogLevel info
# Select the log format
JkLogStampFormat "[%a %b %d %H:%M:%S %Y] "
# JkOptions indicate to send SSL KEY SIZE，
JkOptions +ForwardKeySize +ForwardURICompat -ForwardDirectories
# JkRequestLogFormat set the request format
JkRequestLogFormat "%w %V %T"
# Send servlet for context /examples to worker named ajp13
#JkMount /servlet/* ajp13
# Send JSPs for context /examples to worker named ajp13
JkMount /*.jsp ajp13
JkMount /*.do ajp13
```
上面这一行我们设置了了 /*.jsp ajp13 就是说把所有.jsp结尾的文件都由ajp13这个worker交给tomcat处理了，如果应用被映射为一个.do的URL，这样就会出错.解决方法是再添加如下一行： 
代码 
JkMount /*.do ajp13
5. 配置apache2"conf"workers.properties 
代码
```
workers.tomcat_home=E:"Program Files"Apache Software Foundation"Tomcat 5.5
workers.java_home=E:"Program Files"Java"jdk1.5.0_08
worker.list=ajp13
worker.ajp13.port=8009
worker.ajp13.host=localhost #
worker.ajp13.type=ajp13 #
worker.ajp13.lbfactor=1 #
worker.list=ajp13 
worker.ajp13.port=8009 
worker.ajp13.host=localhost #本机，若上面Tomcat主机不为localhost，作相应修改 
worker.ajp13.type=ajp13 #类型 
worker.ajp13.lbfactor=1 #代理数，不用修改
```
第二部分:虚拟主机的配置 
举例配置2个vhost网站 一个是 localhost ，另一个是 www.ok.com 
当然www.ok.com 是虚拟的，本地测试时，应该修改系统中的hosts文件，添加一行 127.0.0.1 www.ok.com 

1：Apache 虚拟主机配置： 
Httpd.conf文件最后添加 
代码 
include D:"server"Apache2"conf"vhost.conf
而vhost.conf内容写 
代码 
```xml
NameVirtualHost *:80 
<VirtualHost *:80> 
ServerAdmin webmaster at localhost 
DocumentRoot "D:/server/Tomcat/webapps/ROOT" 
ServerName localhost 
ErrorLog logs/localhost-error_log 
CustomLog logs/localhost-access_log common 
</VirtualHost>
<VirtualHost *:80> 
ServerAdmin webmaster@dummy-host dot example.com 
DocumentRoot D:/server/www/ 
ServerName www.ok.com 
ErrorLog logs/ok.com-error_log 
CustomLog logs/ok.com-access_log common 
<Location /server-status> # 这样我可以看到apache服务器状态 
SetHandler server-status 
Order deny，allow 
Deny from all 
Allow from localhost 
Allow from www.ok.com 
</Location> 
</VirtualHost>
```
2：Tomcat虚拟主机配置 

添加新的www.ok.com 虚拟主机，在tomcat安装路径"conf"server.xml的最后，找到<Engine>段，改为 
代码 
```xml
<Engine> 
<Host name=”localhost” ……> 
</Host>
<Host name="www.ok.com" debug="0" appBase="D:/server/www/" unpackWARs="true" autoDeploy="true" xmlValidation="false" xmlNamespaceAware="false"> 
<Context path="" docBase="." /> 
<Logger className="org.apache.catalina.logger.FileLogger" directory="logs" prefix="ok.com_log." suffix=".txt" timestamp="true" /> 
</Host> 
</Engine>
```
3：测试虚拟主机效果 
访问http://localhost/ 应该可以看到原来的tomcat默认页面。 
写一个 index.jsp 
代码 
```html
<html> 
<title> 
test jsp 
</title> 
<% 
String showMessage="Oh My God!"; 
out.print(showMessage); 
%> 
</html>
```
放在d:/server/www下面，访问 http://www.ok.com 
页面显示Oh My God! 就成功了
