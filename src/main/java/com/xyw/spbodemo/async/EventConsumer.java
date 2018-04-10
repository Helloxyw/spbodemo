package com.xyw.spbodemo.async;

import com.alibaba.fastjson.JSONObject;
import com.xyw.spbodemo.controller.IndexController;
import com.xyw.spbodemo.model.EntityType;
import com.xyw.spbodemo.util.JedisAdapter;
import com.xyw.spbodemo.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    //register the event handle function
                    config.get(type).add(entry.getValue());
                }
            }
        }
        //start the thread to consume the event
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //kepp consuming
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    //the first one is the name of the queue
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel eventModel = JSONObject.
                                parseObject(message, EventModel.class);
                        //find the handler lists of the event
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("the unrecognition event");
                            continue;
                        }
                        for (EventHandler eventHandler : config.get(eventModel.getEventType())) {
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
