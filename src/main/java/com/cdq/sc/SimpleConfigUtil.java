package com.cdq.sc;

import org.springframework.util.StringUtils;

/**
 * @Author caodongquan
 * @Date 2022/12/8 9:40
 * @discription
 **/
public class SimpleConfigUtil {

    /**
     * 将入参转换成驼峰结构返回
     */
    public static String toCamel(String str){
        if (!StringUtils.hasLength(str)){
            return null;
        }
        String[] s = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (String value : s) {
            //首字母大写，其余字母小写
            sb.append(value.substring(0, 1).toUpperCase()).append(value.substring(1).toLowerCase());
        }
        return sb.toString();
    }

}
