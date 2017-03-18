package edu.libsys.data.dao;

import edu.libsys.data.mapper.AuthorMapper;
import edu.libsys.entity.Author;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class AuthorDao implements Serializable {
    public Author getUserById(int id) {
        Author author = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            author = authorMapper.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public int addAuthor(Author author) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.addAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return status
        return status;
    }

    public int updateAuthor(Author author) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.updateAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int deleteAuthor(Author author) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.deleteAuthor(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int likeCountPlusOne(int id) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.likeCountPlusOne(id);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int disLikeCountPlusOne(int id) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.disLikeCountPlusOne(id);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public List<Author> getAuthorList() {
        List<Author> authorList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorList = authorMapper.getAuthorList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorList;
    }

    public int countAuthor() {
        int count = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            count = authorMapper.countAuthor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Author> getAuthorListBySearchName(String keyWord) {
        List<Author> authorList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorList = authorMapper.getAuthorListBySearchName(keyWord);
            System.out.println(authorList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorList;
    }
}
