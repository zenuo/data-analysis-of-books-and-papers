package edu.libsys.data.mapper;

import edu.libsys.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BookMapper {
    @Results({
            @Result(property = "id", column = "id"),
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

    @Select("SELECT id, marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE id=#{id}")
    Book getBookById(final int id);

    @Select("SELECT COUNT(*) FROM BOOK")
    int countBook();

    @Select("SELECT id, marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE title like CONCAT('%', #{keyWord}, '%') LIMIT 20")
    List<Book> getBookListBySearchTitle(final String keyWord);

    @Select("SELECT id, marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE author like CONCAT('%', #{keyWord}, '%') LIMIT 20")
    List<Book> getBookListBySearchAuthor(final String keyWord);

    @Select("SELECT id, marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount FROM BOOK WHERE id>((#{page}-1)*#{size}) ORDER BY id ASC LIMIT #{size}")
    List<Book> getBookList(@Param("page") final int page, @Param("size") final int size);

    @Insert("INSERT INTO BOOK(marcRecId, callId, title, author, publisher, pubYear, isbn, likeCount, disLikeCount) VALUES(#{marcRecId}, #{callId}, #{title}, #{author}, #{publisher}, #{pubYear}, #{isbn}, #{likeCount}, #{disLikeCount})")
    void addBook(final Book book);

    @Update("UPDATE BOOK SET likeCount=likeCount+1 id=#{id}")
    void likeCountPlusOne(final int id);

    @Update("UPDATE BOOK SET disLikeCount=disLikeCount+1 id=#{id}")
    void disLikeCountPlusOne(final int id);

    @Update("UPDATE BOOK SET id=#{id}, callId=#{callId}, title=#{title}, author=#{author}, publisher={publisher}, pubYear=#{pubYear}, isbn=#{isbn} WHERE marcRecId=#{marcRecId}")
    void updataBook(final Book book);

    @Delete("DELETE FROM BOOK WHERE id=#{id}")
    void deleteBook(final int marcRecId);
}
