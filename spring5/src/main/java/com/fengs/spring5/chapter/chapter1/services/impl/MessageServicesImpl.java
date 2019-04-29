package com.fengs.spring5.chapter.chapter1.services.impl;

import com.fengs.spring5.chapter.chapter1.services.MessageHandler;
import com.fengs.spring5.chapter.chapter1.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageServices")
public class MessageServicesImpl implements MessageServices {

    private MessageHandler handler;

    @Override
    public String getMessage(MessageHandler handler) {
        if (handler==null) {
            throw new RuntimeException("hi fengs, handler autowired is error!!!");
        }
        return handler.invoke();
    }

    public MessageHandler getHandler() {
        return handler;
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }
}
