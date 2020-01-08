快速开发框架
===========

### maven打包命令
* server与api一起打包
```
mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter,bluesky-framework-security-spring-boot-starter
mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-core,bluesky-framework-server,bluesky-framework-api
```
成功后，在server和api下有target文件夹，文件夹下的jar包就是打的包,如bluesky-***-server-1.0.0-SNAPSHOT.jar

* 单独server打包
```
mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter
mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-core,bluesky-framework-server
```

* 单独api打包
```
mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter,bluesky-framework-security-spring-boot-starter
mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-api
```

### 发布服务器运行
* windows运行参考根目录下的deploy_api.bat、deploy_server.bat
* linux运行参考根目录下的deploy_api.sh、deploy_server.sh
* 发布运行还可以修改运行端口、数据库地址、redis地址等，只需要在运行的jar后面添加 --相应的需要覆盖的配置即可，如：
```
java -jar bluesky-XXX-1.0.0-SNAPSHOT.jar --server.port=8008 --info.domain=192.168.1.100 --spring.dubbo.registry="redis://192.168.1.100:7777" --spring.redis.host=192.168.1.100 --spring.redis.port=7777
```

### 本地运行
* host文件
1. db.local.net为本地单元测试的数据库地址，建议为本地
2. db.bluesky.net 为运行的数据库地址
3. dubbo.bluesky.net 为dubbo注册中心redis的地址
```
修改系统的hosts文件，添加
192.168.1.30 db.local.net
192.168.1.30 db.bluesky.net
192.168.1.136 dubbo.bluesky.net
```
* server运行
```
bluesky-framework-server的src/main/包根目录下，FrameworkServerApplication为可运行类
右键run或者debug就可以运行了
```
* api运行
```
bluesky-framework-api的src/main/包根目录下，FrameworkApiApplication为可运行类
右键run或者debug就可以运行了
```

### 数据库脚本
```
数据库脚本路径：
bluesky-framework-core/test/resources/database/schema/init.ddl
```

### 端口说明

| 模块名称 | 模块项目名称 | groupId |
|----------|-------|-------|
| 快速开发框架server | bluesky-framework-server | 25 |
| 快速开发框架api | bluesky-framework-api | 26 |


| 模块项目名称 | 端口 |
|----------|-------|
| bluesky-framework-server 服务端口 | 2508 |
| bluesky-framework-server dubbo端口 | 2507 |
| bluesky-framework-api 服务端口| 2608 |


### 角色字符串
| 角色类型 | 字符串 |
|----------|-------|
| 系统管理员 | ROLE_PLATFORM,ROLE_ADMIN |
| 政务单位  | ROLE_PLATFORM,ROLE_GOVERNMENT|
| 个人认证  | ROLE_SOCIAL,ROLE_PERSONAL_AUTH|
| 企业认证  | ROLE_SOCIAL,ROLE_COMPANY_AUTH|

### dubbo使用
* bluesky-framework-server配置
1. pom文件添加
```
        <dependency>
            <groupId>org.mvnsearch.spring.boot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```
2. application.properties
```
# 当前dubbo发布的应用名称
spring.dubbo.app=dubbo-framework-provider
# redis注册中心地址及端口
spring.dubbo.registry=redis://dubbo.bluesky.net:6379
# dubbo协议dubbo
spring.dubbo.protocol=dubbo
# 当前dubbo服务运行的端口
spring.dubbo.port=2507
```
3. manager实现类中，使用注解@DubboService
```
@Component
@DubboService
public class AccountAuthorityManagerImpl implements AccountAuthorityManager {
```

* bluesky-framework-spring-boot-starter配置
1. AutoConfiguration继承DubboBasedAutoConfiguration，
2. 添加注解@AutoConfigureAfter(DubboAutoConfiguration.class)
3. 注入manager的bean，@Bean public ReferenceBean xxxManager()
```
@Configuration
@EnableConfigurationProperties(FrameworkProperties.class)
@AutoConfigureAfter(DubboAutoConfiguration.class)
public class FrameworkAutoConfiguration extends DubboBasedAutoConfiguration {
    @Autowired
    private FrameworkProperties properties;

    /**
     * setting
     **/
    @Bean
    public ReferenceBean pageSettingManager() {
        return getConsumerBean(PageSettingManager.class, properties.getVersion(), properties.getTimeout());
    }
    ...
}
```

* bluesky-framework-api 配置
1. pom文件
```
        <dependency>
            <groupId>com.bluesky.framework</groupId>
            <artifactId>bluesky-framework-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

2. application.properties或者application.yml
```
spring:
    # dubbo
    dubbo:
        # dubbo客户端名称
        app: dubbo-bluesky-framework-api
        # dubbo注册中心redis地址及端口
        registry: redis://dubbo.bluesky.net:6379
        # 协议
        protocol: dubbo
        # 线程数
        threads: 200
```

3. 配置好后，直接在代码中就使用了， 和本地bean一样实现服务
```
controller中直接注入manager就可以了

    @Autowired
    private RegionManager regionManager;
    
然后方法中直接regionManager.xxx方法调用了
```

### spring security
* bluesky-framework-security-spring-boot-starter
```
spring security 全局的配置都在bluesky-framework-security-spring-boot-starter
bluesky-framework-api的pom中只要添加：
        <dependency>
            <groupId>com.bluesky.framework</groupId>
            <artifactId>bluesky-framework-security-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

然后只需要配置：
1. URL权限控制
FrameworkSecurityConfigurerAdapter类，继承FrameworkWebSecurityConfigurerAdapter，Override internalConfigure


@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class FrameworkSecurityConfigurerAdapter extends FrameworkWebSecurityConfigurerAdapter {

    @Override
    protected void internalConfigure(HttpSecurity http) throws Exception {}
    
    该方法内进行URL的权限控制：从上到下一次判断，permitAll为都可以访问，hasRole为要有某个角色权限才可以
     .antMatchers("/config/**").permitAll()
     .antMatchers("/setting/page/**").hasRole("ADMIN");
     
     用户的角色权限在account的authoritiesText中
     
2. 接口权限控制
controller中的接口方法上注解PreAuthorize即可
@PreAuthorize("hasPermission('crm-api','om_user_add')")
hasPermission第一个参数目前没实际作用，只是标识当前domain，第二个参数为用户的操作权限代码，最终与用户的操作权限进行对比
```