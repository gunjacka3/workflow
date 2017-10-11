# workflow
基于开源工作流activiti构建工作流平台

## 介绍

基于springmvc框架构建

### 源码

工作流平台源代码请从 https://github.com/gunjacka3/workflow 下载

### JDK 7+

工作流平台运行在JDK7及更高版本 Go to http://www.oracle.com/technetwork/java/javase/downloads/index.html and click on button "Download JDK".  There are installation instructions on that page as well. To verify that your installation was successful, run "java -version" on the command line.  That should print the installed version of your JDK.

### 贡献

提交请至 https://github.com/gunjacka3/workflow/wiki

### 技术选型

#### 后端
+ 核心框架：Spring Framework 4.0
+ 安全框架：spring security
+ 视图框架：Spring MVC 4.0
+ 工作流引擎：Activiti 5.22
+ 任务调度：quartz 2.2.2
+ 持久层框架：MyBatis 3.2.8
+ 数据库连接池：Alibaba Druid 1.0.31
+ 缓存框架：Ehcache 2.6、Redis
+ 日志管理：SLF4J 1.7、Log4j2
+ 工具类：Apache Commons、Jackson 2.2、POI

#### 前端
+ JS框架：JQuery 1.9。
+ CSS框架：Twitter Bootstrap 3。
+ 客户端验证：JQuery Validation Plugin 1.11。
+ restful接口展现：swagger


#### 平台
+ 服务器中间件：在Java EE 5规范（Servlet 2.5、JSP 2.1）下开发，支持应用服务器中间件
有Tomcat 6+、Jboss 7、WebLogic 10、WebSphere 8。
+ 数据库支持：OracleSqlServer 2008、MySql 5.5、H2
+ 开发环境：Java EE、Eclipse、Maven、Git
