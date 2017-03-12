package edu.libsys.data.dao;

import edu.libsys.data.mapper.AuthorMapper;
import edu.libsys.entity.Author;
import org.apache.ibatis.session.SqlSession;

import java.util.stream.Stream;

public class AuthorDao {
    public Author get(int id) {
        SqlSession sqlSession = SessionFactory.getSqlSession();
        Author author = null;
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            author = authorMapper.select(id);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return author;
    }

    public int add(Author author) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.insert(author);
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

    public int update(Author author) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.update(author);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int delete(int id) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.delete(id);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int like(int id) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.like();
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int disLike(int id) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.disLike();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public Stream<Author> gets(int pageNo) {
        return null;
    }

    public int count() {
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
            authorMapper.count();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }
}
