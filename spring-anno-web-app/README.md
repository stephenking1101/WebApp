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
   ```xml
    upstream backend {
        server 10.10.49.23:8080 max_fails=1 fail_timeout=10s;
        server 10.10.49.15:8081 max_fails=1 fail_timeout=10s;
    }
    ```

   修改nginx.conf的location成
   ```xml
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
