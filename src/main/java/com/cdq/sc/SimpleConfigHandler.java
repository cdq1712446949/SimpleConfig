package com.cdq.sc;

import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @Author caodongquan
 * @Date 2022/12/7 16:01
 * @discription
 **/
@Configuration
public class SimpleConfigHandler{

    /** 通过id查询 */
    public static Map<String, Object> byId = new SCMap<>();
    /** 通过key查询 */
    public static Map<String, Object> byKey = new SCMap<>();
    /** 通过分组名称查询 */
    public static Map<String, List<Object>> byGroup = new SCMap<>();
}
