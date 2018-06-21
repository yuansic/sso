# 单点登录服务端

### 入口
	http://ip:port/x-sso-server-web/login
### 部署前提
	需要部署disconf
### 部署修改配置文件
	``` xml
	/x-sso-server-web/src/main/resources/paas/paas-conf.properties
	```
### 单点登录客户端配置

##### 1.web.xml增加
```xml
<filter>
  <filter-name>xss-firewall</filter-name>
  <filter-class>com.x.xss.filter.XSSFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>xss-firewall</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
<filter>
  <filter-name>sessionFilter</filter-name>
  <filter-class>com.x.uni.session.filter.CacheSessionFilter</filter-class>
  <init-param>
    <param-name>ignore_suffix</param-name>
    <param-value>.png,.jpg,.jpeg,.gif,.css,.js,.html,.htm</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>sessionFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
<filter>
  <filter-name>ssoProxyFilter</filter-name>
  <filter-class>com.x.sso.client.filter.FilterChainProxy</filter-class>
  <init-param>
    <param-name>ignore_resources</param-name>
    <param-value>.png,.jpg,.jpeg,.gif,.css,.js,.html,.htm</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>ssoProxyFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
<filter>
  <filter-name>assembleUserInfoFilter</filter-name>
  <filter-class>com.ai.baas.op.web.filter.AssembleUserInfoFilter</filter-class>
  <init-param>
    <param-name>ignore_suffix</param-name>
    <param-value>.png,.jpg,.jpeg,.gif,.css,.js,.html,.htm</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>assembleUserInfoFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```
##### 2.resources下增加sso.properties文件
```properties
# 单点登录服务器登录地址(外网)
casServerLoginUrl=http://172.16.13.159:8083/x-sso-server-web/login
# 单点登录服务器登录地址(内网)
casServerLoginUrl_Inner=http://172.16.13.159:8083/x-sso-server-web/login
# cas 服务器地址(外网)  http://IP:PORT/CasWebProName
casServerUrlPrefix=http://172.16.13.159:8083/x-sso-server-web
# cas 服务器地址(内网)  http://IP:PORT/CasWebProName
casServerUrlPrefix_Inner=http://172.16.13.159:8083/x-sso-server-web
# 单点登录客户端主机地址(外网) http://IP:PORT
serverName=http://172.16.63.87:8084
# 单点登录客户端主机地址(内网) http://IP:PORT
serverName_Inner=http://172.16.13.158:8084
encoding=utf-8
# loginType=S
# serverName=http://10.1.228.222:14211
# 单点登录登出地址(外网) http://IP:PORT/CasWebProName
logOutServerUrl=http://172.16.13.159:8083/x-sso-server-web/logout
# 单点登录登出地址(内网) http://IP:PORT/CasWebProName
logOutServerUrl_Inner=http://172.16.13.159:8083/x-sso-server-web/logout
# 单点登录登出后返回地址(外网) http://IP:PORT/CasWebProName
logOutBackUrl=http://172.16.13.158:8084/ic-sso-client
# 单点登录登出后返回地址(内网) http://IP:PORT/CasWebProName
logOutBackUrl_Inner=http://172.16.13.158:8084/ic-sso-client

# internationalization locale
# 获取用户语言的URL参数名
localeParamName=lang
# 获取用户语言的cookie名
localeCookieName=locale
# Inner Domains
# 单点登录登出后返回地址(自动判断内外网)
innerDomains=xxx.com
```
##### 3.配置disconf缓存文件：redis.properties
```properties
mcs.maxTotal=1024
mcs.maxIdle=200
mcs.minIdle=5
mcs.host=172.16.13.121:6389
mcs.testOnBorrow=true
mcs.password=
```


##### 常用sql
```sql
/**查询有效用户信息*/
SELECT * FROM devcommondb.sys_user where del_flag = '0';
/**查询管理员用户*/
select * from sys_user where id in (SELECT user_id 
FROM devcommondb.sys_user_role where role_id in (1,3,4) and user_id in (SELECT id
FROM devcommondb.sys_user where del_flag = '0'));
/**查询应用系统对应的域名*/
SELECT id, tenant_id, system_id, system_name, remarks, SYSTEM_URL_CONTEXT, del_flag, create_by, create_date, update_by, update_date 
FROM devcommondb.gn_tab_system
```


##### 常见部署问题

###### 问题一
```
19-Apr-2018 11:05:43.163 INFO [localhost-startStop-1] org.apache.catalina.startup.HostConfig.deployWAR Deploying web application archive /data/devweb01/applications/x-sso-server-web/webapps/
x-sso-server-web.war
19-Apr-2018 11:05:46.606 SEVERE [localhost-startStop-1] org.apache.catalina.core.ContainerBase.addChildInternal ContainerBase.addChild: start: 
 org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[/x-sso-server-web]]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:167)
        at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:752)
        at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:728)
        at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:734)
        at org.apache.catalina.startup.HostConfig.deployWAR(HostConfig.java:952)
        at org.apache.catalina.startup.HostConfig$DeployWar.run(HostConfig.java:1823)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
        at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.IllegalStateException: Unable to complete the scan for annotations for web application [/x-sso-server-web] due to a StackOverflowError. Possible root causes include a too low setting for -Xss and illegal cyclic inheritance dependencies. The class hierarchy being processed was [org.bouncycastle.asn1.ASN1EncodableVector->org.bouncycastle.asn1.DEREncodableVector->org.bouncycastle.asn1.ASN1EncodableVector]
        at org.apache.catalina.startup.ContextConfig.checkHandlesTypes(ContextConfig.java:2110)
        at org.apache.catalina.startup.ContextConfig.processAnnotationsStream(ContextConfig.java:2054)
        at org.apache.catalina.startup.ContextConfig.processAnnotationsJar(ContextConfig.java:2000)
        at org.apache.catalina.startup.ContextConfig.processAnnotationsUrl(ContextConfig.java:1970)
        at org.apache.catalina.startup.ContextConfig.processAnnotations(ContextConfig.java:1923)
        at org.apache.catalina.startup.ContextConfig.webConfig(ContextConfig.java:1163)
        at org.apache.catalina.startup.ContextConfig.configureStart(ContextConfig.java:775)
        at org.apache.catalina.startup.ContextConfig.lifecycleEvent(ContextConfig.java:299)
        at org.apache.catalina.util.LifecycleBase.fireLifecycleEvent(LifecycleBase.java:94)
        at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5087)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        ... 10 more

```
修改%TOMCAT_HOME%/confcatalina.properties

添加bcprov*.jar

```
junit.jar,junit-*.jar,hamcrest-*.jar,easymock-*.jar,cglib-*.jar,\
objenesis-*.jar,ant-launcher.jar,\
cobertura-*.jar,asm-*.jar,dom4j-*.jar,icu4j-*.jar,jaxen-*.jar,jdom-*.jar,\
jetty-*.jar,oro-*.jar,servlet-api-*.jar,tagsoup-*.jar,xmlParserAPIs-*.jar,\
xom-*.jar,\
bcprov*.jar
```