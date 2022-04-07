package com.huangxj.common.core.service.impl;

import com.huangxj.common.core.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: MessageServiceImpl
 * @Description: TODO
 * @Author: huangxj
 * @Create: 2018/11/16
 * @Version: 1.0
 **/
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String var1) {

        return getMessage(var1,null);
    }

    @Override
    public String getMessage(String var1, Object[] var2) {
        List<CompletableFuture<String>> list = new ArrayList<>();
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(var1,var2,locale);
    }

    @Override
    public String getMessage(String var1, Object[] var2, String var3, Locale var4) {
        return messageSource.getMessage( var1,var2,var3,var4);
    }

    @Override
    public String getMessage(String var1, Object[] var2, Locale var3) throws NoSuchMessageException {
        return messageSource.getMessage(var1,var2,var3);
    }

    @Override
    public String getMessage(MessageSourceResolvable var1, Locale var2) throws NoSuchMessageException {
        return messageSource.getMessage(var1,var2);
    }
}
