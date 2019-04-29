package com.fengs.spring5.chapter.chapter1.services.impl;

import com.fengs.spring5.chapter.chapter1.services.MessageHandler;
import org.springframework.stereotype.Service;

@Service("messageHandler")
public class MessageHandlerImpl implements MessageHandler {

    @Override
    public String invoke() {
        return "messasge -- class:"+this.getClass().getName();
    }
}
