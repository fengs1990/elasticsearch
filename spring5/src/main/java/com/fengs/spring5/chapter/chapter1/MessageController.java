package com.fengs.spring5.chapter.chapter1;

import com.fengs.spring5.chapter.chapter1.services.MessageHandler;
import com.fengs.spring5.chapter.chapter1.services.MessageServices;
import com.fengs.spring5.chapter.chapter1.services.impl.MessageHandlerImpl;
import com.fengs.spring5.chapter.chapter1.services.impl.MessageServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageHandler handler;

    @GetMapping("/get")
    public String getMessage() {
        MessageServices service = new MessageServicesImpl();
        return service.getMessage(handler);
    }
}
