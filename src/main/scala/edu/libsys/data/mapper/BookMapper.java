package edu.libsys.data.mapper;

import edu.libsys.entity.Book;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BookMapper {
    @Results({
            @Result(property = "marcRecId", column = "marcRecId"),
            @Result(property = "callId", column = "callId"),
            @Result(property = "title", column = "title"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "pubYear", column = "pubYear"),
            @Result(property = "isbn", column = "isbn"),
            @Result(property = "likeCount", column = "likeCount"),
            @Result(property = "disLikeCount", column = "disLikeCount")
    })

    @Select("SELECT * FROM BOOK WHERE marcRecId=#{marcRecId}")
    Book getBookById(String marcRecId);


}
