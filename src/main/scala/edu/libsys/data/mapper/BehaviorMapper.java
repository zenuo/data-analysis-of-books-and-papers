package edu.libsys.data.mapper;

import edu.libsys.entity.Behavior;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BehaviorMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "itemId", column = "itemId"),
            @Result(property = "type", column = "type"),
            @Result(property = "time", column = "time"),
            @Result(property = "content", column = "content")
    })

    @Select("SELECT id, userId, itemId, type, time, content FROM BEHAVIOR WHERE id=#{id}")
    Behavior getBehaviorById(int id);

    @Insert("INSERT INTO BEHAVIOR(userId, itemId, type, time, content) VALUES(#{userId}, #{itemId}, #{type}, #{time}, #{content})")
    void addBehavior(Behavior behavior);

    @Select("SELECT id, userId, itemId, type, time, content FROM BEHAVIOR")
    List<Behavior> getBehaviorList();

    @Select("SELECT id, userId, itemId, type, time, content FROM BEHAVIOR WHERE userId=#{userId}")
    List<Behavior> getBehaviorListByUserId(int userId);

    @Select("SELECT id, userId, itemId, type, time, content FROM BEHAVIOR WHERE itemId=#{itemId}")
    List<Behavior> getBehaviorListByItemId(int itemId);
}
