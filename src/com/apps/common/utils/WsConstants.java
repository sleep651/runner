package com.apps.common.utils;

/**
 * WebService常量定义.
 * 
 */
public class WsConstants {
    
    /**
     * 以下为通用返回代码(手机版也包含在内)
     */
    public static final Short SHT_SUCCESS = 1;//成功;
    public static final Short SHT_VALIDATION = 0;//失败
    public static final Short SHT_ERROR = -1;//服务端异常
    public static final Short SHT_NO_SESSION = 2;//无效用户
}
