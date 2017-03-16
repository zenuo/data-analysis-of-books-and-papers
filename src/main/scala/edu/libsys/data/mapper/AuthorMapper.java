package edu.libsys.data.mapper;

import edu.libsys.entity.Author;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AuthorMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "workCount", column = "workCount"),
            @Result(property = "likeCount", column = "likeCount"),
            @Result(property = "disLikeCount", column = "disLikeCount")
    })

    @Select("SELECT id, name, workCount, likeCount, disLikeCount FROM AUTHOR WHERE id=#{id}")
    Author getUserById(int id);

    @Select("SELECT id, name, workCount, likeCount, disLikeCount FROM AUTHOR")
    List<Author> getAuthorList();

    @Select("SELECT COUNT(*) FROM AUTHOR")
    int countAuthor();

    @Select("SELECT id, name, workCount, likeCount, disLikeCount FROM AUTHOR WHERE name like CONCAT('%',#{keyWord},'%')")
    List<Author> getAuthorListBySearchName(String keyWord);

    @Insert("INSERT INTO AUTHOR(name, workCount, likeCount, disLikeCount) VALUES(#{name} ,#{workCount}, #{likeCount}, #{disLikeCount})")
    void addAuthor(Author author);

    @Update("UPDATE AUTHOR SET name=#{name} ,workCount=#{workCount} WHERE id=#{id}")
    void updateAuthor(Author author);

    @Update("UPDATE AUTHOR SET likeCount=likeCount+1 WHERE id=#{id}")
    void likeCountPlusOne(int id);

    @Update("UPDATE AUTHOR SET disLikeCount=disLikeCount+1 WHERE id=#{id}")
    void disLikeCountPlusOne(int id);

    @Delete("DELETE FROM AUTHOR WHERE id=#{id}")
    void deleteAuthor(Author author);

}
