package com.xyw.spbodemo.controller;

import com.xyw.spbodemo.async.EventModel;
import com.xyw.spbodemo.async.EventProducer;
import com.xyw.spbodemo.async.EventType;
import com.xyw.spbodemo.model.EntityType;
import com.xyw.spbodemo.model.HostHolder;
import com.xyw.spbodemo.model.News;
import com.xyw.spbodemo.service.LikeService;
import com.xyw.spbodemo.service.NewsService;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId) {

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        //update the likeCount
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE).
        setActorId(hostHolder.getUser().getId()).
                setEntityId(newsId).
                setEntityType(EntityType.ENTITY_NEWS)
                .setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId") int newsId) {

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
