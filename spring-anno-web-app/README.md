mvn archetype:generate -DgroupId=com.superware -DartifactId=spring-anno-web-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

������װTomcat

����A����ȡTomcat��װ��

wget http://apache.etoak.com/tomcat/tomcat-7/v7.0.14/bin/apache-tomcat-7.0.14.tar.gz

����B����ѹ����װ

mkdir -p /usr/local/webserver/
tar  xvzf  apache-tomcat-7.0.14.tar.gz -C /usr/local/webserver/
#��������װĿ¼
cd /usr/local/webserver/
mv apache-tomcat-7.0.14/ tomcat

����C������tomcat�Զ������û�����

#���һ��ϵͳ�û�tomcat����������Ϊ���ɵ�¼ϵͳ�� 
useradd -d /usr/local/webserver/tomcat -s /usr/sbin/nologin tomcat

����D�������û�tomcat��tomcatĿ¼�ķ���Ȩ��

chown -R tomcat.tomcat /usr/local/webserver/tomcat/

����E������Tomcat��������

vi /etc/environment
 
CATALINA_BASE=/usr/local/webserver/tomcat
CATALINA_HOME=/usr/local/webserver/tomcat
TOMCAT_USER��tomcat

����ʹ����������ʹ������Ч

. /etc/environment

����F������Tomcat

/usr/local/webserver/tomcat/bin/startup.sh 
 
Using CATALINA_BASE:   /usr/local/webserver/tomcat
Using CATALINA_HOME:   /usr/local/webserver/tomcat
Using CATALINA_TMPDIR: /usr/local/webserver/tomcat/temp
Using JRE_HOME:        /usr
Using CLASSPATH:       /usr/local/webserver/tomcat/bin/bootstrap.jar:/usr/local/webserver/tomcat/bin/tomcat-juli.jar

����G������

��������http://ip:8080/���������Tomcatȱʡ����ͱ�ʾ�ɹ��ˡ�

����H��ֹͣTomcat

/usr/local/webserver/tomcat/bin/shutdown.sh

����I������Tomcat����Ա�ʺ�

�����ڵı�ǩǰ�����������

nano /usr/local/webserver/tomcat/conf/tomcat-users.xml
 
<role rolename="admin-gui"/>
<role rolename="admin-script"/>
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<user username="admin" password="000000" roles="manager-gui,manager-script,manager-jmx,manager-status,admin-script,admin-gui"/>

��������رպ���������tomcat�����������涨�����û��������룬www.linuxidc.com���¼Tomcat�Ĺ���ҳ�档

����J�����ػ����̷�ʽ����tomcat

��������tomcat�ٷ���Ҫ��tomcat��Ϊһ���ػ��������У���Ҫ�õ�jsvc����

������װjsvc

cd  /usr/local/webserver/tomcat/bin/
tar xvzf  commons-daemon-native.tar.gz 
cd commons-daemon-1.0.5-native-src/unix/
./configure
make
cp jsvc ../..
cd ../..

����������������������ػ���������tomcat

cd  /usr/local/webserver/tomcat/

��������http://ip:8080/���������Tomcatȱʡ����ͱ�ʾ�ɹ��ˡ�

����K�����ÿ�������tomcat

�������������ػ�����ʽ���ÿ��������ģ��о�N��δ��������ʱ������ķ����ɣ�
����
�����༭/etc/rc.local�����������ű�

vi /etc/rc.local
 
/usr/local/webserver/tomcat/bin/startup.sh

��������,����http://ip:8080/���������Tomcatȱʡ����ͱ�ʾ�ɹ��ˡ�

    L. ����GZIP
�޸�%TOMCAT_HOME%/conf/server.xml���޶��ڵ����£�
<Connector port="80" protocol="HTTP/1.1"   
           connectionTimeout="20000"   
           redirectPort="8443" URIEncoding="utf-8"   
                       compression="on"   
                       compressionMinSize="50" noCompressionUserAgents="gozilla, traviata"   
                       compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain" /> 
                       
                       ����Tomcat�̳߳���ʹ�ø߲�������


    M.
1.�򿪹�����̳߳أ�

<Service name="Catalina">  
  <!--The connectors can use a shared executor, you can define one or more named thread pools-->  
  
    <Executor name="tomcatThreadPool" namePrefix="catalina-exec-"    
    maxThreads="1000" minSpareThreads="50" maxIdleTime="600000"/>

Ĭ��ǰ����ע��<!-- -->���ģ�ȥ���Ϳ����ˡ�

��Ҫ����˵����

name�������̳߳ص����֡�����ConnectorΪ�˹����̳߳�Ҫ���õ����֣������ֱ���Ψһ��Ĭ��ֵ��None��

namePrefix:��JVM�ϣ�ÿ�������̶߳�������һ��name �ַ�������һ����Ϊ�̳߳���ÿ���̵߳�name�ַ���������һ��ǰ׺��Tomcat�����̺߳�׷�ӵ���һǰ׺�ĺ��档Ĭ��ֵ��tomcat-exec-��

maxThreads�����̳߳ؿ������ɵ�����߳�����Ĭ��ֵ��200��

maxIdleTime����Tomcat�ر�һ�������߳�֮ǰ����������̳߳�����ʱ��(�Ժ���Ϊ��λ)��ֻ�е�ǰ��Ծ���߳�������minSpareThread��ֵ���Ż�رտ����̡߳�Ĭ��ֵ��60000(һ����)��

minSpareThreads��TomcatӦ��ʼ�մ򿪵���С����Ծ�߳�����Ĭ��ֵ��25��

threadPriority���̵߳ĵȼ���Ĭ����Thread.NORM_PRIORITY

 

2. ��Connector��ָ��ʹ�ù����̳߳أ�

<Connector executor="tomcatThreadPool"
           port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
           minProcessors="5"
           maxProcessors="75"
           acceptCount="1000"/>

 

��Ҫ����˵����
executor����ʾʹ�øò���ֵ��Ӧ���̳߳أ�

minProcessors������������ʱ�����Ĵ���������߳�����

maxProcessors�������Դ����Ĵ���������߳�����

acceptCount��ָ�������п���ʹ�õĴ���������߳�������ʹ��ʱ�����Էŵ���������е�����������������������󽫲��账��