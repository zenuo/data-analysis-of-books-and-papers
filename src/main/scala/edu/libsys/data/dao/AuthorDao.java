package edu.libsys.data.dao;

import edu.libsys.data.mapper.AuthorMapper;
import edu.libsys.entity.Author;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class AuthorDao {
    public Author getUserById(int id) {
        SqlSession sqlSession = SessionFactory.getSqlSession();
        Author author = null;
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            author = authorMapper.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return author;
    }

    public int addAuthor(Author author) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.addAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        //return status
        return status;
    }

    public int updateAuthor(Author author) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.updateAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int deleteAuthor(Author author) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.deleteAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int likeCountPlusOne(int id) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.likeCountPlusOne();
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int disLikeCountPlusOne(int id) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.disLikeCountPlusOne();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    //TO-DO
    public List<Author> gets(int pageNo) {
        List<Author> authorList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            RowBounds rowBounds = new RowBounds(0, 10);
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorList = authorMapper.getAuthorList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return authorList;
    }

    public int countAuthor() {
        int count = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            count = authorMapper.countAuthor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    public List<Author> getAuthorListBySearchName(String keyWord) {
        List<Author> authorList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            RowBounds rowBounds = new RowBounds(0, 10);
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorList = authorMapper.getAuthorListBySearchName(keyWord);
            System.out.println(authorList.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return authorList;
    }
}
