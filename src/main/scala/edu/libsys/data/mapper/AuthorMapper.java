package edu.libsys.data.mapper;

import edu.libsys.entity.Author;
import org.apache.ibatis.annotations.*;

public interface AuthorMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "workCount", column = "workCount"),
            @Result(property = "likeCount", column = "likeCount"),
            @Result(property = "disLikeCount", column = "disLikeCount")
    })

    @Select("SELECT * FROM AUTHOR WHERE id=#{id}")
    Author select(int id);

    @Insert("INSERT INTO AUTHOR(id, name, workCount) VALUES(#{id}, #{name} ,#{workCount})")
    void insert(Author author);

    @Update("UPDATE AUTHOR SET name=#{name} ,workCount=#{workCount} WHERE id=#{id}")
    void update(Author author);

    @Delete("DELETE FROM AUTHOR WHERE id=#{id}")
    void delete(int id);

    @Update("UPDATE AUTHOR SET likeCount=likeCount+1 WHERE id=#{id}")
    void like();

    @Update("UPDATE AUTHOR SET disLikeCount=disLikeCount+1 WHERE id=#{id}")
    void disLike();

    @Select("SELECT COUNT(*) FROM AUTHOR")
    int count();
}
