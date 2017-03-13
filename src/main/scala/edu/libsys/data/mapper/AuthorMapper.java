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

    @Select("SELECT * FROM AUTHOR WHERE id=#{id}")
    Author getUserById(int id);

    @Select("SELECT * FROM AUTHOR")
    List<Author> getAuthorList();

    @Insert("INSERT INTO AUTHOR(name, workCount, likeCount, disLikeCount) VALUES(#{name} ,#{workCount}, #{likeCount}, #{disLikeCount})")
    void addAuthor(Author author);

    @Update("UPDATE AUTHOR SET name=#{name} ,workCount=#{workCount} WHERE id=#{id}")
    void updateAuthor(Author author);

    @Delete("DELETE FROM AUTHOR WHERE id=#{id}")
    void deleteAuthor(Author author);

    @Update("UPDATE AUTHOR SET likeCount=likeCount+1 WHERE id=#{id}")
    void likeCountPlusOne();

    @Update("UPDATE AUTHOR SET disLikeCount=disLikeCount+1 WHERE id=#{id}")
    void disLikeCountPlusOne();

    @Select("SELECT COUNT(*) FROM AUTHOR")
    int countAuthor();

    @Select("SELECT * FROM AUTHOR WHERE name like CONCAT('%',#{keyWord},'%')")
    List<Author> getAuthorListBySearchName(String keyWord);
}
