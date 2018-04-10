package com.xyw.spbodemo.controller;


import com.xyw.spbodemo.dao.MessageDao;
import com.xyw.spbodemo.model.HostHolder;
import com.xyw.spbodemo.model.Message;
import com.xyw.spbodemo.model.User;
import com.xyw.spbodemo.model.ViewObject;
import com.xyw.spbodemo.service.MessageService;
import com.xyw.spbodemo.service.UserService;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = {"/msg/list"}, method = RequestMethod.GET)
    public String conversationDetail(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("target", user);
                vo.set("userName",user.getName());
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("failed to get the letter" + e.getMessage());
        }

        return "letter";
    }


    @RequestMapping(path = {"/msg/detail"}, method = RequestMethod.GET)
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("msg", message);
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());

                messages.add(vo);

            }
            model.addAttribute("messages", messages);

        } catch (Exception e) {
            logger.error("failed to get the conversation" + e.getMessage());
        }


        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId)
                    : String.format("%d_%d", toId, fromId));

            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());
        } catch (Exception e) {
            logger.error("failed to add message" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "failed to add message");
        }

    }
}
