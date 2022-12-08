package com.cdq.sc;

/**
 * @Author caodongquan
 * @Date 2022/12/7 16:20
 * @discription
 **/
public class SystemConfigEntityNotFoundException extends RuntimeException{

    public SystemConfigEntityNotFoundException(String message){
        super("System Config Entity Not Found,Class Name:" + message);
    }
}
