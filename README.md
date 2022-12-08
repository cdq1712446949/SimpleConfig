# SimpleConfig

## 简介

### Key-Value处理

- 在日常开发中我们经常会遇到Key-Value形式的系统配置，SimpleConfig会统一处理这些配置

### 系统配置处理流程
    1.项目启动后自动加载系统配置表
    2.根据id，key，group生成三个map
    3.map中的数据在增删改之后都会同步到数据库

### 数据库连接

### 1.DeriverManager


    DriverManager之所以会被放弃，是因为DriverManger对数据的存取是建立与
    数据库的连接，但是这种方式在后续的发展中面对频繁的数据库交互会产生较大的系统
    开销，所以才会有了DataSource(连接池)
    
    SimpleConfig是为了处理系统配置而生，系统配置本省就不会有过多的数据库交互，所以
    SimpleConfig配置了DriverManager的连接方式


### 2.DataSource


