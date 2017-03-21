package edu.libsys.data.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class SessionFactory implements Serializable {
    //static sqlSessionFactory
    public static SqlSessionFactory sqlSessionFactory;

    //build sqlSessionFactory, return a instance of SqlSession.
    public static SqlSession getSqlSession() {
        String resource = "edu/libsys/conf/mybatis-config.xml";
        if (sqlSessionFactory == null) {
            try {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                inputStream.close();
            } catch (IOException e) {
                System.err.println("Build SqlSessionFactory error.");
                e.printStackTrace();
            }
        }
        return sqlSessionFactory.openSession();
    }
}
