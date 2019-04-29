package com.fengs.spring5.chapter.chapter1.services;

import org.springframework.beans.factory.annotation.Autowired;

public interface MessageServices {

    public String getMessage(MessageHandler handler);
}
