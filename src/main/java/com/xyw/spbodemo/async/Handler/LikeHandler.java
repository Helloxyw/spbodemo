package com.xyw.spbodemo.async.Handler;

import com.xyw.spbodemo.async.EventHandler;
import com.xyw.spbodemo.async.EventModel;
import com.xyw.spbodemo.async.EventType;
import com.xyw.spbodemo.model.Message;
import com.xyw.spbodemo.model.User;
import com.xyw.spbodemo.service.MessageService;
import com.xyw.spbodemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(3);
        //message.setToId(eventModel.getEntityOwnerId());
        message.setToId(eventModel.getActorId());
        int fromId = message.getFromId();
        int toId = message.getToId();

        User user = userService.getUser(eventModel.getActorId());
        message.setContent("the user :" + user.getName() +
                " liked your advisory,http://127.0.0.1:8080/news/" + eventModel.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        System.out.println("liked");
    }

    @Override
    public List<EventType> getSupportEventTypes() {

        return Arrays.asList(EventType.LIKE);
    }
}
