package com.huangxj.common.core.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author huangxj
 * @version 1.0
 * @date 2020/11/25 9:14
 */
public class GlobalThreadMap {

    public static ConcurrentHashMap<String,Thread> parkThreadMap = new ConcurrentHashMap();

}
