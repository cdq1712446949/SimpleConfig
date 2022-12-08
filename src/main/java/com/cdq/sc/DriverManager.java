package com.cdq.sc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author caodongquan
 * @Date 2022/10/30 14:34
 * @discription 以DeriverManager的方式连接数据库
 **/
@Configuration
@ConfigurationProperties("sc.db")
public class DriverManager implements InitializingBean {

    private String url;
    private String userName;
    private String password;
    private String className;
    private String tableName;
    private String idColumn;
    private String keyColumn;
    private String valueColumn;
    private String groupColumn;
    private final List<String> otherColumn = new ArrayList<>();

    private Connection getConnection() {
        try {
            //1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接对象
            return java.sql.DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<?> query(String sql, Class<?> clazz) {
        //1.获取数据库连接
        Connection connection = getConnection();
        List<Object> result = new ArrayList<>();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            //2.执行sql语句，获取查询结果
            ResultSet resultSet = statement.executeQuery(sql);
            String[] columnNames = getColNames(resultSet);
            Method[] ms = clazz.getMethods();
            Object o = null;
            while (resultSet.next()){
                //通过反射装配对象
                o = clazz.getDeclaredConstructor().newInstance();
                for (String colName : columnNames) {
                    String methodName = "set" + SimpleConfigUtil.toCamel(colName);
                    //稳妥一点。在对象查询下是否有此方法在调用方法
                    for (Method md : ms) {
                        if (methodName.equals(md.getName())) {
                            Class<?>[] parameterTypes = md.getParameterTypes();
                            if (parameterTypes[0].equals(Integer.class)){
                                md.invoke(o, resultSet.getInt(colName));
                            } else {
                                md.invoke(o, resultSet.getObject(colName));
                            }
                            break;
                        }
                    }
                }
                result.add(o);
            }
            return result;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载系统配置
     */
    private void loadConfig() {
        //1.通过反射获取表结构
        getTable();
        //2.查询数据库数据
        try {
            queryAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //3.把数据放入本地线程中
        System.out.println("存放数据");
    }

    private void getTable() {
        Field[] fields;
        try {
            Class<?> clazz1 = Class.forName(className);
            fields = clazz1.getDeclaredFields();
            Table annotation = clazz1.getAnnotation(Table.class);
            if (null != annotation) {
                tableName = annotation.value();
            }
        } catch (ClassNotFoundException e) {
            throw new SystemConfigEntityNotFoundException(className);
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(IdColumn.class)) {
                IdColumn annotation = field.getAnnotation(IdColumn.class);
                idColumn = annotation.value();
            } else if (field.isAnnotationPresent(KeyColumn.class)) {
                KeyColumn annotation = field.getAnnotation(KeyColumn.class);
                keyColumn = annotation.value();
            } else if (field.isAnnotationPresent(ValueColumn.class)) {
                ValueColumn annotation = field.getAnnotation(ValueColumn.class);
                valueColumn = annotation.value();
            } else if (field.isAnnotationPresent(GroupColumn.class)) {
                GroupColumn annotation = field.getAnnotation(GroupColumn.class);
                groupColumn = annotation.value();
            } else if (field.isAnnotationPresent(Column.class)) {
                Column annotation = field.getAnnotation(Column.class);
                otherColumn.add(annotation.value());
            }
        }
    }

    private void queryAll() throws SQLException {
        Class<?> clazz ;
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(idColumn).append(",").append("`").append(keyColumn).append("`").append(",")
                .append(valueColumn).append(",").append("`").append(groupColumn).append("`");
        otherColumn.forEach(item -> {
            sql.append(",").append(item);
        });
        sql.append(" from ").append(tableName);
        try {
            clazz = Class.forName(className);
            List<?> query = query(sql.toString(), clazz);
            //根据不同条件分组
            for (Object o : query){
                //通过id区分
                Method method = clazz.getMethod("get" + SimpleConfigUtil.toCamel(idColumn));
                String id = (String) method.invoke(o);
                SimpleConfigHandler.byId.put(id, o);
                //通过key区分，组装key-value，方便快速查询
                Method getKey = clazz.getMethod("get" + SimpleConfigUtil.toCamel(keyColumn));
                Method getValue = clazz.getMethod("get" + SimpleConfigUtil.toCamel(valueColumn));
                String key = (String) getKey.invoke(o);
                String value = (String) getValue.invoke(o);
                SimpleConfigHandler.byKey.put(key, value);
                //通过group区分
                Method getGroup = clazz.getMethod("get" + SimpleConfigUtil.toCamel(groupColumn));
                String group = (String) getGroup.invoke(o);
                if (SimpleConfigHandler.byGroup.containsKey(group)){
                    SimpleConfigHandler.byGroup.get(group).add(o);
                } else {
                    List<Object> tempList = new ArrayList<>();
                    tempList.add(o);
                    SimpleConfigHandler.byGroup.put(group, tempList);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SystemConfigEntityNotFoundException(className);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取数据库列名
     * @param  rs
     * @return
     */
    private String[] getColNames(ResultSet rs) throws SQLException {
        ResultSetMetaData red = rs.getMetaData();
        //获取查询的列数
        int count = red.getColumnCount();
        String[] colNames = new String[count];
        for(int i = 1; i <= count; i ++) {
            //获取列名
            colNames[i - 1] = red.getColumnLabel(i);
        }
        return colNames;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void afterPropertiesSet() {
        loadConfig();
    }
}
