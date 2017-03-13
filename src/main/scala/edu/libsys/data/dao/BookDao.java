package edu.libsys.data.dao;

import edu.libsys.data.mapper.BookMapper;
import edu.libsys.entity.Book;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class BookDao {
    public Book getBookByMarcRecId(int marcRecId) {
        Book book = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            book = bookMapper.getBookByMarcRecId(marcRecId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return book;
    }

    public int addBook(Book book) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookMapper.addBook(book);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int updataBook(Book book) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookMapper.updataBook(book);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int deleteBook(Book book) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookMapper.deleteBook(book);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int countBook() {
        int count = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            count = bookMapper.countBook();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    public List<Book> getBookListBySearchTitle(String keyWord) {
        List<Book> bookList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookList = bookMapper.getBookListBySearchTitle(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return bookList;
    }

    public int likeCountPlusOne(Book book) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookMapper.likeCountPlusOne(book);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int disLikeCountPlusOne(Book book) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            bookMapper.likeCountPlusOne(book);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}
