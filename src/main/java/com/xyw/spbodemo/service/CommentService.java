package com.xyw.spbodemo.service;

import com.xyw.spbodemo.dao.CommentDao;
import com.xyw.spbodemo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;


    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public void deleteComment(int entityId, int entityType, int status) {
        commentDao.updateStatus(entityId, entityType, 1);
    }
}
