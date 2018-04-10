package com.xyw.spbodemo.async.Handler;

import com.xyw.spbodemo.async.EventHandler;
import com.xyw.spbodemo.async.EventModel;
import com.xyw.spbodemo.async.EventType;
import com.xyw.spbodemo.model.Message;
import com.xyw.spbodemo.service.MessageService;
import com.xyw.spbodemo.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        //judge whether login exception
        Message message = new Message();
        message.setToId(eventModel.getActorId());
        message.setContent("the last login exception");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", eventModel.getExts("username"));
        mailSender.sendWithHtmlTemplate("1641137052@qq.com",
                "login exception", "mails/welcome", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
