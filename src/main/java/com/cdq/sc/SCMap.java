package com.cdq.sc;

import java.util.HashMap;

/**
 * @Author caodongquan
 * @Date 2022/12/8 10:09
 * @discription
 **/
public class SCMap<K,V> extends HashMap<K,V> {

    /**
     * 添加数据的同时，根据isInsert判断是否向数据库添加记录
     * @param isInsert 是否插入数据库记录
     */
    private V put(K key, V value, boolean isInsert){
        if (isInsert){
            //添加系统配置记录
            System.out.println("插入系统配置记录");
        }
        return super.put(key, value);
    }

    /**
     * 删除数据的同时根据isDelete判断是否删除数据库记录
     * @param isDelete 是否删除数据库记录
     */
    private V remove(Object key, boolean isDelete){
        if (isDelete){
            //删除系统配置记录
        }
        return super.remove(key);
    }

    /**
     * 修改数据的同时根据isReplace判断是否修改数据库记录
     * @param isReplace 是否修改数据库记录
     */
    private V replace(K key, V value, boolean isReplace){
        if (isReplace){
            //修改系统配置记录
        }
        return super.replace(key, value);
    }

}
