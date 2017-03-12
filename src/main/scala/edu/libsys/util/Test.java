package edu.libsys.util;

import edu.libsys.data.dao.SessionFactory;
import org.apache.ibatis.session.SqlSession;

public class Test {
    //test
    public static void main(String[] args) {
        SessionFactory sessionFactory = new SessionFactory();
        SqlSession sqlSession = sessionFactory.getSqlSession();
        sqlSession.getConnection();
        sqlSession.close();
    }
}