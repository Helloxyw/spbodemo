package com.xyw.spbodemo.controller;


import com.xyw.spbodemo.model.*;
import com.xyw.spbodemo.service.*;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;


    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {

        try {
            //filter content
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);

            //update the commentCount of news
            int count = commentService.getCommentCount(
                    comment.getEntityId(), comment.getEntityType());

            newsService.updateCommentCount(comment.getEntityId(), count);

            //how to asynchronous
        } catch (Exception e) {
            logger.error("failed to add comment " + e.getMessage());
        }
        return "redirect:/news/" + newsId;
    }

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);
        if (news != null) {
            int localUserId = hostHolder.getUser() != null ?
                    hostHolder.getUser().getId() : 0;

            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }

            //comment
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }

            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }


    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {

        try {
            News news = new News();
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                //the anonymous user
                news.setUserId(3);
            }

            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "add news failed");
        }
    }

    @RequestMapping(path = {"/image/"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("read  image failed" + e.getMessage());
        }
    }


    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //    String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (file == null) {
                return ToutiaoUtil.getJSONString(1, "upload image failed");
            }

            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("upload image failed" + e.toString());
            return ToutiaoUtil.getJSONString(1, "upload failed");
        }

    }


}
