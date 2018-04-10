package com.xyw.spbodemo.async;


import com.alibaba.fastjson.JSONObject;
import com.xyw.spbodemo.util.JedisAdapter;
import com.xyw.spbodemo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;

    //the triggered event will be put the queue
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
