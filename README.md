# SimpleConfig

### 简介
    简化系统配置表的相关代码
    
### 源码地址
[源码](https://gitee.com/vvwvvwvvw/simple-config)

### 使用方法
1. 引入依赖
    ```xml
   <!-- 需要添加私库地址 -->
    <repositories>
        <repository>
            <id>maven-repository-main</id>
            <name>simple-config</name>
            <url>https://raw.github.com/cdq1712446949/SimpleConfig/prd/</url>
        </repository>
    </repositories>
    <!-- Simple-Config 自动加载系统配置 -->
   <dependencies>
       <dependency>
            <groupId>com.cdq</groupId>
            <artifactId>simple-config</artifactId>
            <version>0.1.2</version>
       </dependency>
   </dependencies>
    ```
2. 添加配置信息
    ```yaml
    sc:
      db:
        url: "jdbc:mysql://localhost:3306/database"
        userName: "root"
        password: "密码"
        # 实际项目中系统配置表的entity
        className: "com.cdq.common.entity.dao.SystemConfig"
    ```
3. 启动项目


    src/main/java/com/cdq/sc/SimpleConfigHandler.java,通过该类中的一下三个map进行系统配置的增删改查
```java

    /** 通过id查询 */
    public static SCMap<String, Object> byId = new SCMap<>(new ByIdSCMapListener());
    /** 通过key查询 */
    public static Map<String, Object> byKey = new SCMap<>();
    /** 通过分组名称查询 */
    public static Map<String, List<Object>> byGroup = new SCMap<>();
```
    
### Key-Value处理

- 在日常开发中我们经常会遇到Key-Value形式的系统配置，SimpleConfig会统一处理这些配置

### 系统配置处理流程
    1.项目启动后自动加载系统配置表
    2.根据id(默认为开启，可以通过sc.db.enableById关闭)，key，group生成三个map
    3.map中的数据在增删改之后都会同步到数据库

### 数据库连接

#### 1.DeriverManager


    DriverManager之所以会被放弃，是因为DriverManger对数据的存取是建立与
    数据库的连接，但是这种方式在后续的发展中面对频繁的数据库交互会产生较大的系统
    开销，所以才会有了DataSource(连接池)
    
    SimpleConfig是为了处理系统配置而生，系统配置本身就不会有过多的数据库交互，所以
    SimpleConfig配置了DriverManager的连接方式


#### 2.DataSource

### 版本更新日志
    
#### 0.1.1
    1.实现项目启动自动加载系统配置
#### 0.1.2
    1.实现map调用put方法自动插入数据库记录
