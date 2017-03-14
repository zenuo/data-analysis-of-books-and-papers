package edu.libsys.data.mapper;

import edu.libsys.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("SELECT marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE marcRecId=#{marcRecId}")
    Book getBookByMarcRecId(int marcRecId);

    @Select("SELECT COUNT(*) FROM BOOK")
    int countBook();

    @Select("SELECT marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE title like CONCAT('%', #{keyWord}, '%')")
    List<Book> getBookListBySearchTitle(String keyWord);

    @Select("SELECT marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK")
    List<Book> getBookList();

    @Insert("INSERT INTO BOOK(marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount) VALUES(#{marcRecId}, #{callId}, #{title}, #{author}, #{publisher}, #{pubYear}, #{isbn}, #{likeCount}, #{disLikeCount})")
    void addBook(Book book);

    @Update("UPDATE BOOK SET likeCount=likeCount+1 marcRecId=#{marcRecId}")
    void likeCountPlusOne(Book book);

    @Update("UPDATE BOOK SET disLikeCount=disLikeCount+1 marcRecId=#{marcRecId}")
    void disLikeCountPlusOne(Book book);

    @Update("UPDATE BOOK SET callId=#{callId}, title=#{title}, author=#{author}, publisher={publisher}, pubYear=#{pubYear}, isbn=#{isbn} WHERE marcRecId=#{marcRecId}")
    void updataBook(Book book);

    @Delete("DELETE FROM BOOK WHERE marcRecId=#{marcRecId}")
    void deleteBook(Book book);
}
