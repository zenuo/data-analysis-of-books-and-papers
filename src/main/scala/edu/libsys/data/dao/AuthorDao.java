package edu.libsys.data.dao;

import edu.libsys.data.daoInterface.AuthorDaoInterface;
import edu.libsys.entity.Author;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import java.util.stream.Stream;

public class AuthorDao implements AuthorDaoInterface {

    private static Logger logger = Logger.getLogger(Author.class);
    private SqlSessionFactory sqlSessionFactory;

    public AuthorDao(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public Author get(int id) {
        return null;
    }

    public int add(Author author) {
        return 0;
    }

    public int update(Author author) {
        return 0;
    }

    public int delete(int id) {
        return 0;
    }

    public int like(int id) {
        return 0;
    }

    public int disLike(int id) {
        return 0;
    }

    public Stream<Author> gets(int pageNo) {
        return null;
    }

    public int count() {
        return 0;
    }
}
