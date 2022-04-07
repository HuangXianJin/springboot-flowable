package com.huangxj.common.core.service;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * @ClassName MessageService
 * @Author: huangxj
 * @Create: 2018/11/26
 **/
public interface MessageService {

   /**
    *  获得信息
    * @param var1
    * @return
    */
   String getMessage(String var1);


   /**
    * 获得信息和pojo
    * @param var1
    * @param var2
    * @return
    */
   String getMessage(String var1, Object... var2);


   /**
    * 获得信息和pojo
    * @param var1
    * @param var2
    * @param var3
    * @param var4
    * @return
    */
   String getMessage(String var1, @Nullable Object[] var2, @Nullable String var3, Locale var4);

   /**
    * 获得信息和pojo
    * @param var1
    * @param var2
    * @param var3
    * @return
    * @throws NoSuchMessageException
    */
   String getMessage(String var1, @Nullable Object[] var2, Locale var3) throws NoSuchMessageException;

   /**
    * 获得信息和pojo
    * @param var1
    * @param var2
    * @return
    * @throws NoSuchMessageException
    */
   String getMessage(MessageSourceResolvable var1, Locale var2) throws NoSuchMessageException;
}
