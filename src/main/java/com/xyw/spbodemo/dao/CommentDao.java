package com.xyw.spbodemo.dao;

import com.xyw.spbodemo.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment";
    String INSERT_FIELDS = "user_id, content, created_date, entity_id, entity_type, status";
    String SELECT_FIELDS = "id ," + INSERT_FIELDS;


    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int etityId, @Param("entityType") int entityType,@Param("status")int status);

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{id},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int etityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int etityId, @Param("entityType") int entityType);
}
