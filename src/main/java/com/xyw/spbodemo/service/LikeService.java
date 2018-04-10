package com.xyw.spbodemo.service;

import com.xyw.spbodemo.util.JedisAdapter;
import com.xyw.spbodemo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private JedisAdapter jedisAdapter;


    //if you like ,return 1
    //if you dislike ,return -1
    // else return 0
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }

        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return jedisAdapter.sismember(dislikeKey,
                String.valueOf(entityId)) ? -1 : 0;
    }

    public long like(int userId, int entityType, int entityId) {
        //add to like list
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //delete from dislike
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));


        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {

        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));


        return jedisAdapter.scard(dislikeKey);
    }
}
