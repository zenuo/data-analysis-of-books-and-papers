package edu.libsys.data.mapper;

import edu.libsys.entity.Behavior;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BehaviorMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "itemId", column = "itemId"),
            @Result(property = "type", column = "type"),
            @Result(property = "time", column = "time"),
            @Result(property = "content", column = "content")
    })

    @Select("SELECT * FROM BEHAVIOR WHERE id=#{id}")
    Behavior select(int id);

    @Insert("INSERT INTO BEHAVIOR(userId, itemId, type, time, content) VALUES(#{userId}, #{itemId}, #{type}, #{time}, #{content})")
    void insert(Behavior behavior);
}
