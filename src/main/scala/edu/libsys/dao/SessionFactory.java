package edu.libsys.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactory {
    //static sqlSessionFactory
    public static SqlSessionFactory sqlSessionFactory;

    //constructor, build sqlSessionFactory.
    public SessionFactory(){
        String resource = "edu/libsys/conf/mybatis-config.xml";
        if (sqlSessionFactory == null){
            try{
                InputStream inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                inputStream.close();
                System.out.println("Build SqlSessionFactory success.");
            }catch (IOException e){
                System.err.println("Build SqlSessionFactory error.");
                e.printStackTrace();
            }
        }
    }
}
